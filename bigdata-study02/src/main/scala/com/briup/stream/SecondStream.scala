package com.briup.stream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types.{DoubleType, IntegerType, LongType, StringType, StructType, TimestampType}

object SecondStream {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    import org.apache.spark.sql.SparkSession;
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("seconds stream test")
      .getOrCreate();
    import spark.implicits._

    //1.如何创建流式的Dataset/DataFrame
    //1.输入源  file Source
    val schema = new StructType()
      .add("productName", StringType)
      .add("nums", IntegerType)
      .add("stayTime", DoubleType)
      .add("isColl", IntegerType)
      .add("buyNums", IntegerType)
      .add("eventTime", LongType)

    val df = spark.readStream
      .format("csv")
      .schema(schema)
      .option("sep", ":")
      .option("path", "data/out1/")
      .load()

    println("df是否为流式:" + df.isStreaming)

    //ds.show()
    //启动流查询 行动操作
    val query1 = df.writeStream
      .format("console")
      .start()
    //让主程序等待流查询执行完毕
    query1.awaitTermination();
    spark.stop()
  }
}
