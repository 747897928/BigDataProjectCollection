package com.briup.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

object CatalogTest {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark: SparkSession =SparkSession.builder()
      .master("local[*]")
      .appName("catalog对象")
      //.config("spark.sql.warehouse.dir","file:/H:/eclipse/spark2020/spark-warehouse/")
      .getOrCreate()
    import spark.implicits._
    val sc=spark.sparkContext

    val catalog=spark.catalog;
    catalog.listDatabases().show(false)
    //加载外部数据为外部表
    //catalog.createTable("students","data/students.json","json")
    //val dataFrame = catalog.createTable("students","data/students.csv","csv")
    val dataFrame = spark.read.format("csv").load("data/students.csv")
    //dataFrame.write.mode(SaveMode.Overwrite).saveAsTable("student");
    dataFrame.write.saveAsTable("students");
    catalog.listTables().show(false);
    spark.stop();
  }
}
