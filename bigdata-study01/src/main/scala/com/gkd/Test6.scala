package com.gkd

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Column, DataFrame, Dataset, SparkSession}

/**
 * @author zhaoyijie
 * @since 2024/9/6 11:41
 */
object Test6 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("test6").master("local[2]")
      .config("spark.sql.streaming.checkpointLocation", "data/test6")
      .getOrCreate()
    import spark.implicits._
    val sc = spark.sparkContext
    sc.setLogLevel("WARN");
    /*val frame1 = spark.readStream
      .format("csv")
      .schema("name string,gender string,score int")
      .option("header", "true")
      .load("file:///Users/zhaoyijie/IdeaProjects/bigDataHomeWork/data/ssa_names")
    frame1.printSchema()
    frame1.writeStream.format("json").option("path", "file:///Users/zhaoyijie/IdeaProjects/bigDataHomeWork/data/test6")
      .start().awaitTermination()*/

    val rddFile: RDD[String] = sc.textFile("data/ssa_names/yob1999.txt")
    val dataSet: Dataset[(String, String, String)] = rddFile.map(line => {
      val arr = line.split(",")
      (arr(0), arr(1).trim(), arr(2))
    }).toDS()
    val frame: DataFrame = dataSet.toDF("name", "gender", "score")
    frame.createOrReplaceTempView("table")
    val dataFrame: DataFrame = spark.sql("select name,sum(score) as sum_score from table group by name")
    dataFrame.show(true)
    //dataFrame.union()//并集
    //dataFrame.intersect()//交集
    //dataFrame.except()//差集
    spark.catalog.dropTempView("table")
    dataFrame.write.format("json").save("data/test6")
    sc.stop()
    spark.close();
  }
}
