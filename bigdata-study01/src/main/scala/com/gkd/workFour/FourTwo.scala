package com.gkd.workFour

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/23 15:03
 */
object FourTwo {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf();
    conf.setMaster("local[*]").setAppName("MySparkTestTwo");
    val sc = new SparkContext(conf);
    val data = sc.textFile("data/Data01.txt");
    val courseList = data.map(x => x.split(",")(1));
    courseList.distinct();
    println(courseList.count());
  }
}
