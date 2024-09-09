package com.gkd.workFive

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import java.util.Properties
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/30 15:45
 */
object WorkFiveTwo {
    def main(args: Array[String]) {
      Logger.getLogger("org").setLevel(Level.ERROR)
      val spark=SparkSession.builder()
        .master("local[*]")
        .appName("workFive")
        .getOrCreate()
      import spark.implicits._
      val sc=spark.sparkContext
      val employeeRDD = sc.parallelize(Array("3 Mary F 26","4 Tom M 23")).map(_.split(" "))
      val schema=StructType(List(
        StructField("id", IntegerType, true),
        StructField("name", StringType, true),
        StructField("gender", StringType, true),
        StructField("age", IntegerType, true)));
      val rowRDD = employeeRDD.map(p => Row(p(0).toInt,p(1).trim, p(2).trim,p(3).toInt))
      val employeeDF = spark.createDataFrame(rowRDD, schema)
      val prop = new Properties()
      prop.put("user", "root")
      prop.put("password", "zy1999125")
      prop.put("driver","com.mysql.jdbc.Driver")
      prop.put("serverTimezone","GMT")
      employeeDF.write.mode("append")jdbc("jdbc:mysql://localhost:3306/sparktest",
        "sparktest.employee", prop)
        val jdbcDF = spark.read.format("jdbc")
          .option("url", "jdbc:mysql://localhost:3306/sparktest")
          .option("driver","com.mysql.jdbc.Driver")
          .option("dbtable","employee")
          .option("serverTimezone","GMT")
          .option("user","root")
          .option( "password", "zy1999125")
          .load()
      jdbcDF.agg("age" -> "max", "age" -> "sum").show()
    }
}
