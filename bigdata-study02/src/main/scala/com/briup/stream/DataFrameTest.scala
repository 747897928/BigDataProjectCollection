package com.briup.stream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types._
import org.junit.Test

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/3/10 14:51
 */
class DataFrameTest {
  Logger.getLogger("org").setLevel(Level.ERROR)

  import org.apache.spark.sql.SparkSession;
  val spark = SparkSession.builder()
    .master("")
    .appName("spark stream test")
    .getOrCreate();

  import spark.implicits._


  @Test
  def SocketSpark(): Unit = {
    val df = spark.readStream.
      format("socket")
      .option("host", "localhost")
      .option("port", "9999")
      .option("includeTimestamp", "true")
      .load();
    println(df.isStreaming)
    import org.apache.spark.sql.functions.window
    val result = df.groupBy(window($"timestamp"
      , "30 seconds", "5 seconds"), $"value")
      .count()
    val query = result.writeStream
      .format("console")
      .option("truncate", "false")
      .outputMode(OutputMode.Update())
      .start()
    query.awaitTermination();
  }

  @Test
  def testFileSource(): Unit = {
    //2.获取流式DataFrame/Dataset
    val schema = new StructType()
      .add("id", IntegerType, false)
    val df = spark.readStream
      .format("orc")
      .schema(schema)
      .option("path", "data/out/save_orc")
      .load()
    println(df.isStreaming)
    //3.调用方法
    //不支持show foreach
    //    df.show()
    // 1 to 10  2 to 11
    val ds = df.as[Int]
    val result = ds.map(_ + 1)
    //result.foreach(x=>println(x))
    //4.启动流查询
    val query = result.writeStream
      //指定接收器 skin 输出目的地 console kafka fileSource ...
      .format("console")
      //指定输出模式 append update complete
      .outputMode("append")
      .start()
    //5.等待流查询执行结束
    query.awaitTermination()
  }


  @Test
  def testFileSource1(): Unit = {
    //2.获取流式DataFrame/Dataset
    val schema = new StructType()
      .add("name", StringType)
      .add("num1", IntegerType)
      .add("num2", DoubleType)
      .add("num3", IntegerType)
      .add("num4", IntegerType)
      .add("num5", LongType)
    val df = spark.readStream
      .format("csv")
      .option("sep", ":")
      .option("path", "data/out1")
      .schema(schema)
      .load()
    println(df.isStreaming)
    //3.调用方法
    //不支持show foreach
    df.createOrReplaceTempView("shopinfo")
    val result = spark.sql("select name,num5 from shopinfo")
    //4.启动流查询
    val query = result.writeStream
      //指定接收器 skin 输出目的地 console kafka fileSource ...
      .format("console")
      //指定输出模式 append update complete
      .outputMode("append")
      .start()
    //5.等待流查询执行结束
    query.awaitTermination()
  }
}
