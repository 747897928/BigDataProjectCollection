package com.gkd.workFour

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/23 14:49
 */
object FourOne {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf();
    conf.setMaster("local[*]").setAppName("MySparkTestOne");
    val sc = new SparkContext(conf);
    val data = sc.textFile("data/Data01.txt");
    val stdList = data.map(x => x.split(",")(0));
    stdList.distinct();
    println(stdList.count());
  }
}
