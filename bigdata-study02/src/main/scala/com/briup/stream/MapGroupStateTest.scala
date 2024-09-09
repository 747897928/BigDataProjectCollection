package com.briup.stream

import java.sql.Timestamp

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{GroupState, GroupStateTimeout}

/**
 * 重点：学习任意状态操作
 * 业务需求：
 * 从网络数据源中获取数据
 * 数据格式: 商品,商品的关注度
 * eg:棉花,8.5
 * a,7.6
 * 要求:获取五分钟内的对应商品平均关注度以及最新的关注度
 **/
object MapGroupStateTest {
  def main(args: Array[String]): Unit = {
    //1.获取SparkSession对象
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("test")
      //开启类型推断
      .config("spark.sql.streaming.schemaInference", "true")
      .getOrCreate()
    import spark.implicits._
    //2.获取流式DataFrame对象
    val shopDF = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", "8888")
      .option("includeTimestamp", "true")
      .load()
      .toDF("info", "time")
      .as[(String, Timestamp)]
    //3.根据DataFrame方法进行业务逻辑处理
    //    val transformDF=shopDF.map( x => {
    //      x match {
    //        case (info,time) =>
    //          val Array(shopname,rank)=info.split(",")
    //          (shopname,rank,time)
    //      }
    //    })
    val transformDF = shopDF.map {
      case (info, time) =>
        val Array(shopname, rank) = info.split(",")
        (shopname, rank.toDouble, time)
    }.toDF("shopname", "rank", "time")
      .as[(String, Double, Timestamp)]

    import org.apache.spark.sql.functions.window
    //获取五分钟内的对应商品平均关注度以及最新的关注度
    //    val resultDF=transformDF.withWatermark("time","30 seconds")
    //      .groupBy(window($"time","2 minutes","1 minutes"),$"shopname")
    //      .avg("rank")

    //(shopname,avgRank,newRank)
    // func: (K, Iterator[V], GroupState[S]) => U
    //任意状态操作  S 状态类型 有用户自定义 (String,List[(String,Double,Timestamp)])
    val func = (key: String, iter: Iterator[(String, Double, Timestamp)]
                , state: GroupState[(String, List[(String, Double, Timestamp)])]) => {
      val list = iter.toList
      if (state.hasTimedOut) {
        state.remove()
      } else if (state.exists) {
        val (shopname, beforeList) = state.get
        val nowState = (shopname, beforeList ++ list)
        //更新状态
        state.update(nowState)
      } else {
        //第一次出现 初始化 更新状态
        val init = (key, list)
        state.update(init)
        state.setTimeoutDuration("2 minutes")
      }
      //返回
      if (state.exists) {
        val (_, allDataList) = state.get
        //正常输出
        val avgRank = allDataList.map(_._2).sum.toDouble / allDataList.size
        val newRank = list.maxBy(_._3.getTime)._2
        (key, avgRank, newRank)
      } else {
        (key, 0.0, 0.0)
      }
    }
    val resultDF = transformDF.groupByKey(_._1)
      .mapGroupsWithState(GroupStateTimeout.ProcessingTimeTimeout())(func)
      .toDF("shopname", "avgRank", "newRank")


    //4.启动流查询
    val query = resultDF.writeStream
      .format("console")
      .outputMode("update")
      .start()
    //5.等待流查询运行结束
    query.awaitTermination()
    spark.stop()

  }
}
