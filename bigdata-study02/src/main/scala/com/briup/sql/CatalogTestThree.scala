package com.briup.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.lit

/**
 *
 *
 * @author zhaoyijie
 * @since 2022/7/6 15:09
 */
object CatalogTestThree {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("catalog对象")
      //.config("spark.sql.warehouse.dir","file:/H:/eclipse/spark2020/spark-warehouse/")
      .getOrCreate()
    import spark.implicits._
    val sc = spark.sparkContext

    val catalog = spark.catalog;
    catalog.listDatabases().show(false)
    //加载外部数据为外部表
    //catalog.createTable("students","data/students.json","json")
    //val dataFrame = catalog.createTable("students","data/students.csv","csv")
    var dataFrame: DataFrame = spark.read.parquet("data/sparkTest/");
    //dataFrame.write.mode(SaveMode.Overwrite).saveAsTale("student");
    dataFrame.show(true);
    dataFrame = dataFrame.withColumnRenamed("_c0", "stuNum")
    dataFrame = dataFrame.withColumn("label_id", lit(1))
    dataFrame.show(true)
    catalog.listTables().show(false);
    spark.stop();
  }

}
