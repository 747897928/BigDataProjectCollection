package com.gkd

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.junit.Test

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/29 10:13
 */
class Test5 {
  import java.util.Properties
  import org.apache.spark.sql.types._
  import org.apache.spark.sql.Row

  Logger.getLogger("org").setLevel(Level.ERROR)
  val spark=SparkSession.builder()
    .master("local[*]")
    .appName("test")
    .getOrCreate()
  import spark.implicits._
  val sc=spark.sparkContext
  @Test
  def testOne(): Unit ={

    val studentRDD = sc.parallelize(Array("3 Rongcheng M 26","4 Guanhua M 27")).map(_.split(" "))
    val schema = StructType(Array(StructField("id", IntegerType, true),StructField("name", StringType, true),StructField("gender", StringType, true),StructField("age", IntegerType, true)))
    val rowRDD = studentRDD.map(rowElem => Row(rowElem(0).toInt,
      rowElem(1).trim, rowElem(2).trim, rowElem(3).toInt))
    val studentDF = spark.createDataFrame(rowRDD, schema)
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "zy1999125")
    prop.put("driver","com.mysql.jdbc.Driver")
    prop.put("serverTimezone","GMT")

    studentDF.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "spark.student", prop)

  }
}
