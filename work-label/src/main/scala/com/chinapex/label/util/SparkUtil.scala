package com.chinapex.label.util

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType

/**
  * Spark相关工具类
  */
object SparkUtil {

  private var fileSystem : FileSystem = null

  /**
    * 构建spark Session
    * @param master
    * @param appName
    * @return
    */
  def buildSpark(master : String, appName : String) : SparkSession ={
    val spark : SparkSession = SparkSession.builder()
      .appName(appName)
      .master(master)
      //.config("spark.sql.warehouse.dir",hivePath + s"$database.db")
      .enableHiveSupport.getOrCreate()
    spark
  }


  /**
    *
    * @param audiences
    * @param sparkSession
    */
  def foreachText (sparkSession: SparkSession, path : String,
                   f : (String, SparkSession) => scala.Unit): Unit = {
    sparkSession.sparkContext
      .textFile(path)
      .foreach(line => f(line, sparkSession))
  }

  /**
    * 任务耗时
    * @param action
    * @tparam T
    * @return
    */
  def timeCost[T](action: => T): Long = {
    val begin = System.currentTimeMillis()
    action
    System.currentTimeMillis() - begin
  }

  /**
    * 获取FileSystem
    * @return
    */
  def getFileSystem () : FileSystem = {
    if (null == fileSystem) {
      val conf = new Configuration()
      fileSystem = FileSystem.get(conf)
    }

    fileSystem
  }

  /**
   * 根据DataFrame的StructType生成HiveDDL
   * @param schema
   * @param location
   * @param speedTime
   * @return
   */
  def generateHiveDdlWithStructType (schema : StructType, location : String, table : String, speedTime : Long, tableComment : String = ""): String = {
    // 基于DataFrame.schema生成hive建表语句的body体部分
    val sqlBody = schema.sql.replace(":", "")
      .replace("STRUCT<","")
      .replace(">","")

    s"""
       |CREATE EXTERNAL TABLE IF NOT EXISTS ${table}
       |(
       |   ${sqlBody}
       |)
       |COMMENT
       |   '${tableComment}'
       |ROW FORMAT SERDE
       |  'org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe'
       |STORED AS INPUTFORMAT
       |  'org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat'
       |OUTPUTFORMAT
       |  'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat'
       |LOCATION
       |  '${location}'
       |TBLPROPERTIES
       |  (
       |     'speed_time'='${speedTime}'
       |  )
       |
    """.stripMargin
  }

  def buildSparkSession(master: String, appName: String): SparkSession = {
    println(s"master:${master}, appName:${appName}")
    val spark = SparkSession.builder().master(master).appName(appName).getOrCreate()
    spark
  }

  def buildSparkSessionConf(master: String, appName: String, conf: SparkConf): SparkSession = {
    println(s"master:${master}, appName:${appName}, conf:${conf}")
    val spark = SparkSession.builder().master(master).appName(appName).config(conf).getOrCreate()
    spark
  }



}
