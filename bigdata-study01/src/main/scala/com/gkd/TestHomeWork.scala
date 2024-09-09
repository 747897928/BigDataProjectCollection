package com.gkd

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.junit.{After, AfterClass, Before, BeforeClass, Test}

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/7/1 10:57
 */
class TestHomeWork {
  val spark=SparkSession.builder()
    .master("local[*]")
    .appName("test")
    .getOrCreate()
  import spark.implicits._
  val sc=spark.sparkContext
  @Before
  def beforeFun(): Unit ={
    Logger.getLogger("org").setLevel(Level.ERROR)
  }
  @Test
  def testOne(): Unit ={
    val dataFrame = spark.read.json("data/people.json");
    println(dataFrame.show())
  }
  @After
  def afterFun(): Unit ={
    println("测试结束方法")
  }
}
