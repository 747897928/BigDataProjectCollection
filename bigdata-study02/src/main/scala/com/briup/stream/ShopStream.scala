package com.briup.stream

import java.sql.Timestamp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{GroupState, GroupStateTimeout, OutputMode}

/**
 * 计算商品的实时关注度以及不间断连续五分钟的平均关注度
 *
 * 本项目的数据来源是使用脚本模拟用户浏览商品产生实时数据,数据包括用户当前浏览的商品以及浏览商品的次数和停留时间和是否收藏该商品。
 **/
object ShopStream {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    //1.创建SparkSession对象
    val spark = SparkSession.builder()
      .appName("Stream Project")
      //指定运行模式为 Yarn
      .master("yarn")
      .getOrCreate()
    import spark.implicits._
    //2.获取流式DataFrame对象
    val kafkaDF = spark.readStream
      .format("kafka")
      //192.168.1.157
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "shopInfos")
      .load()
      // 6列 key value topic partition .....
      .selectExpr("cast(value as string)")
      .as[String]
    //mianhua::2::4.5743::0::1::1584004453951
    //3.处理数据 获取商品的实时关注度以及连续不间断五分钟的平均关注度
    val stage1DS = kafkaDF.filter(line => !(line.isEmpty)).map(line => {
      //商品ID::浏览次数::浏览停留时间::是否收藏::购买件数::事件时间
      //val Array(shopname,nums,times,isCol,buyNums,eventTime)=line.trim.split("::")
      val arr = line.trim.split("::")
      val shopname = arr(0)
      val nums = arr(1)
      val times = arr(2)
      val isCol = arr(3)
      val buyNums = arr(4)
      val eventTime = arr(5)
      //约定浏览次数的权重为0.8
      //浏览停留时间权重为0.6
      //是否收藏权重为1
      //购买件数权重为1
      //计算商品的关注度
      val rank = nums.trim.toDouble * 0.8 + times.trim.toDouble * 0.6 + isCol.trim.toInt * 1 + buyNums.trim.toInt * 1
      (shopname, rank, new Timestamp(eventTime.trim.toLong))
    }).toDF("shopname", "rank", "eventTime")
      .as[(String, Double, Timestamp)]

    //处理1:计算连续不断五分钟的平均关注度
    //    stage1DS.groupBy(window(),$"shopname")
    //      .agg("avg"->"rank","max"->"eventTime")
    val func = (shopname: String
                , values: Iterator[(String, Double, Timestamp)]
                , state: GroupState[List[(String, Double, Timestamp)]]) => {
      val list = values.toList
      if (state.hasTimedOut) {
        state.remove()
      } else if (state.exists) {
        //说明状态对象存在，即key在之前的批次中出现过
        //即上一批次存放到状态对象中的values集合
        val oldValues = state.get
        //获取当前以及之前批次中的所有values值(集合)
        val newState = oldValues.union(list)
        //更新状态对象
        state.update(newState)
        //重新设置key出现的间隔时间
        state.setTimeoutDuration("2 minutes")
      } else {
        //说明该key未调用过，第一次调用
        val initState = list
        //更新状态
        state.update(initState)
        //设置key出现的间隔时间 即state对象过时时间
        state.setTimeoutDuration("2 minutes")
      }
      //将最终计算完毕的数据返回
      //window 最大事件时间
      //key 商品名称
      //avg 平均关注度
      //now 实时关注度
      if (state.exists) {
        //数据正常输出
        val newValues = state.get;
        //根据当前的values获取最新的事件对象
        val maxEventTimeValue = list.maxBy(_._3.getTime);
        //获取最大事件时间
        val maxEventTime = maxEventTimeValue._3
        //实时关注度
        val now = maxEventTimeValue._2
        //平均关注度
        val avg = newValues.map(_._2).sum / newValues.size
        (maxEventTime, shopname, avg, now)
      } else {
        //数据不可以使用(之前批次的数据)state.get
        val maxEventTimeValue = list.maxBy(_._3.getTime);
        //获取最大事件时间
        val maxEventTime = maxEventTimeValue._3
        //实时关注度
        val now = maxEventTimeValue._2
        //平均关注度
        val avg = list.map(_._2).sum / list.size
        (maxEventTime, shopname, avg, now)
      }
    }
    val stage2DF = stage1DS.groupByKey(_._1)
      .mapGroupsWithState(GroupStateTimeout.ProcessingTimeTimeout())(func)
      .toDF("window", "goodID", "avgData", "nowData")
    //处理2:输出数据的格式
    val stage3DF = stage2DF.selectExpr("cast(concat('{\"window\":\"',cast(window as String),'\",\"key\":\"',goodID,'\"}') as String) as key", "cast(concat('{\"window\":\"',cast(window as string),'\",\"key\":\"',cast(goodID as String),'\",\"avg\":\"',cast(avgData as String),'\",\"now\":\"',cast(nowData as String),'\"}') as String) as value")
    /**
     *
     * key的数据格式为：
     * {
     * "window":"2018-06-07 13:02:00",
     * "key":"大豆"
     * }
     * value的数据格式为：
     * {
     * "window":"2018-06-07 13:02:00",
     * "key":"大豆",
     * "avg":"8.346788890",
     * "now":"10.35058824"
     * }
     *
     **/
    //4.启动流查询
    val query = stage3DF.writeStream
      .format("kafka")
      .outputMode(OutputMode.Update())
      .option("kafka.bootstrap.servers", "192.168.1.157:9092")
      .option("topic", "zhangsan")
      //      .option("truncate","false")
      .option("checkpointLocation", "spark-checkpoint")
      .start()
    //5.等待流查询运行结束
    query.awaitTermination()
    spark.stop()
  }
}
