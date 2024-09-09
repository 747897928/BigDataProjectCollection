package com.briup.stream.utils

import java.io._
import java.sql.Timestamp

import scala.io.Source
import scala.util.Random

/**
  *【Spark流式计算电商商品关注度】
  * 模拟数据生成器
  * 模拟用户浏览商品的次数，停留时间，以及是否收藏，购买次数
  * */
object SimulatorFile{

  def main(args: Array[String]): Unit = {

    //读取配置文件 获取模拟商品数据的设置信息
    val list=Source.fromFile("src/main/scala/com/briup/stream/utils/conf.properties").getLines().toList;
    val map=list.filter(!_.contains("#")).map(line=>line.split("[=]")).map(arr=>{(arr(0),arr(1))}).toMap;

    //将配置文件中的内容赋值给以下属性
    //假设一共有200个商品
    val GOODSID = map.getOrElse("GOODSID","200").toInt
    //随机发送消息的最大条数
    val MSG_NUM = map.getOrElse("MSG_NUM","30").toInt;
    //假设用户浏览该商品的最大次数
    val BROWSE_NUM = map.getOrElse("BROWSE_NUM","5").toInt;
    //假设用户浏览商品停留的最大时间
    val STAY_TIME = map.getOrElse("STAY_TIME","10").toInt;
    //用来体现用户是否收藏，收藏为1，不收藏为0，差评为-1
    val COLLECTION= map.get("COLLECTION") match {
      case Some(line) => line.split(",").toArray
      case None => Array[Int](-1, 0, 1)
    }
    //用来模拟用户购买商品的件数，0比较多是为了增加没有买的概率，毕竟不买的还是很多的，很多用户都只是看看
    val BUY_NUM=map.get("BUY_NUM") match {
      case Some(line) => line.split(",").toArray
      case None => Array[Int](0, 1, 0, 2, 0, 0, 0, 1, 0)
    }
    //商品名称
    val productList = map.get("product").get.split(",").toList;


    try {
      val basePath=map.get("basePath") match {
        case  Some(path) => path
        case None => throw new RuntimeException("请先在配置文件中配置生成数据之后存储的目录！")
      }
      val r = new Random();
      while (true) {
        //随机消息数
        val msgNum = r.nextInt(MSG_NUM) + 1
        println("开始随机次数："+msgNum)
        //创建数据存储的文件对象
        val file=new File(basePath,"shopInfo-"+new java.util.Date().getTime+".log");
        if(file.exists()){
          file.delete()
        }else{
          file.createNewFile();
        }
        //往文件中存储数据
        val pw = new PrintWriter(new FileOutputStream(file))
        for (i <- 0 to msgNum) {
          //消息格式：商品ID::浏览次数::停留时间::是否收藏::购买件数::事件时间
          val sb = new StringBuffer
//          sb.append("goodsID-" + (r.nextInt(GOODSID) + 1))
          val index=r.nextInt(productList.size);
          sb.append(productList(index))
          sb.append(":")
          sb.append(r.nextInt(BROWSE_NUM) + 1)
          sb.append(":")
          sb.append(r.nextInt(STAY_TIME) + r.nextFloat)
          sb.append(":")
          sb.append(COLLECTION(r.nextInt(2)))
          sb.append(":")
          sb.append(BUY_NUM(r.nextInt(9)))
          sb.append(":")
          sb.append(new java.util.Date().getTime)
          println(sb.toString)
          //发送消息
          pw.write(sb.toString() + "\n");
        }
        pw.flush();
        pw.close();

        //停顿5s再次运行上边的程序，生成新的文件数据
        try {
          Thread.sleep(5000);
        } catch{
          case _:InterruptedException =>
            println("thread sleep failed");
          case e: Throwable =>
            println(e.getMessage)
        }
        println("休息时间到了！")
      }
    }catch{
      case e:Exception => e.printStackTrace();
    }
  }
}

