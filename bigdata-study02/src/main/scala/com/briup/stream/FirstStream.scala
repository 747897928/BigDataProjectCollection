package com.briup.stream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types.{IntegerType, LongType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row}

/**
 * 第一个Structed Stream
 **/
object FirstStream {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    import org.apache.spark.sql.SparkSession;
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("spark stream test")
      .getOrCreate();
    import spark.implicits._

    //1.如何创建流式的Dataset/DataFrame
    //1.输入源  file Source
    val schema = new StructType()
      .add("id", LongType)
      .add("name", StringType)
      .add("age", IntegerType)
      .add("score", IntegerType)
      .add("className", StringType)

    val ds = spark.readStream
      .format("csv")
      .schema(schema)
      .option("sep", ",")
      .option("path", "data/students/")
      .load().as[(Long, String, Int, Int, String)]

    println("ds是否为流式:" + ds.isStreaming)

    //ds.show()
    //启动流查询 行动操作
    val query1 = ds.writeStream
      .format("console")
      .start()
    //让主程序等待流查询执行完毕
    query1.awaitTermination();
    spark.stop()
  }
}
