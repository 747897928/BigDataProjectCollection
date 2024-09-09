package com.gkd

import org.apache.spark.sql.{DataFrame, RelationalGroupedDataset, SparkSession}
import org.junit.Test

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/28 10:21
 */
class Test4 {
  val spark=SparkSession.builder()
    .master("local[*]")
    .appName("test")
    .getOrCreate()
  import spark.implicits._
  val sc=spark.sparkContext
   @Test
   def testOne(): Unit ={
     val dataFrame = spark.read.json("data/people.json");
     println(dataFrame.show())
   }
  @Test
  def testTwo(): Unit ={
    val dataFrame1 = spark.read.parquet("data/users.parquet");
    println(dataFrame1.show())
  }
  @Test
  def testThree(): Unit ={
    val dataFrame1 = spark.read.parquet("data/users.parquet");
    println(dataFrame1.show())
    dataFrame1.write.json("test.json")
  }
}
