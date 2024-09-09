package com.briup.stream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.streaming.OutputMode

/**
 * 词频统计
 **/
object WordCountStream {
  def main(args: Array[String]): Unit = {
    //spark
    Logger.getLogger("org").setLevel(Level.ERROR)
    import org.apache.spark.sql.SparkSession;
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("word count stream test")
      .getOrCreate();
    import spark.implicits._

    //1.从网络数据源获取实时数据
    val df = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load();
    //2.词频统计
    val wordAndNumDF = df.flatMap(row => {
      row.getString(0).split(" ")
    }).map(word => (word, 1))
      .toDF("word", "num")
    val result = wordAndNumDF.groupBy("word")
      .sum("num")
    //3.启动流查询
    val query = result.writeStream
      .format("console")
      .outputMode(OutputMode.Complete())
      .start()
    query.awaitTermination()
    spark.stop()
  }
}
