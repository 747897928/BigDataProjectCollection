package com.briup.stream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.Duration
import org.apache.spark.streaming.Seconds

/**
  * Spark Streming简单例子
  * */
object StreamTest {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("NetworkWordCount")

    val ssc = new StreamingContext(conf, Seconds(2))

    val lines = ssc.textFileStream("logfile")

    //将每一行接收到的数据通过空格分割成单词
    val words = lines.flatMap(_.split(""))
    //导入StreamingContext中的隐式转换
    import org.apache.spark.streaming.StreamingContext._
    // 对每一批次的单词进行转化求和
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)
    // 每个批次中默认打印前十个元素到控制台
    wordCounts.print()

    ssc.start()         // 开始计算
    ssc.awaitTermination()  // 等待计算终止 7*24
    ssc.stop()       //结束应用

  }
}
