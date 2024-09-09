package com.gkd.workFour

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/23 15:05
 */
object FourThree {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf();
    conf.setMaster("local[*]").setAppName("MySparkTestThree");
    val sc = new SparkContext(conf);
    val data = sc.textFile("data/Data01.txt");
    val tomList = data.filter(x => x.split(",")(0)=="Tom");
    tomList.foreach(println);
    val array = tomList.map(row => (row.split(",")(0), row.split(",")(2).toInt))
      .mapValues(x => (x, 1))
      .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
      .mapValues(x => (x._1 / x._2)).collect();
    array.foreach(println)
  }
}
