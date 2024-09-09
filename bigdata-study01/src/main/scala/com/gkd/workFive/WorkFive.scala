package com.gkd.workFive

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SparkSession}

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/30 15:24
 */
object WorkFive {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark=SparkSession.builder()
      .master("local[*]")
      .appName("workFive")
      .getOrCreate()
    import spark.implicits._
    val sc=spark.sparkContext
    val employeeRDD = sc.textFile("data/employee.txt")
    val fields = Array("id","name","age")
      .map(fieldName=> StructField(fieldName, StringType, nullable = true))
    val schema = StructType(fields)
    val rowRDD = employeeRDD.map(_.split(",")).map(attributes
        =>Row(attributes(0).trim(), attributes(1), attributes(2).trim()))
    val employeeDF = spark.createDataFrame(rowRDD, schema)
    employeeDF.createOrReplaceTempView("employee")
    val results = spark.sql("select id,name,age from employee")
    results.map(line => s"id:${line(0)},name:${line(1)},age:${line(2)}").show(false);
  }
}
