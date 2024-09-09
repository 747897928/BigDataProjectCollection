package com.chinapex.label.audience

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.json4s.jackson.Serialization

import scala.util.Random

/**
 * 简化版的人群计算
 */
object AudienceCalculate {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").appName("AudienceCalculate").getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._
    // val df = spark.read.json("hdfs://192.168.1.101:9000/user/hive/warehouse/edt/ods/*.json")
    //下面的语句代替读取hdfs

    //用户主表，里面的字段省略
    val masterDf: DataFrame = spark.read.csv("data/audience/master.csv").toDF("id", "name")
    masterDf.printSchema()
    masterDf.persist(StorageLevel.MEMORY_AND_DISK)
    masterDf.createOrReplaceTempView("master")
    //主要是获得用户的id
    //val masterIdDF = masterDf.select("id").distinct()
    import org.apache.spark.sql.functions.col
    val masterIdDF = masterDf.select(col("id"))

    //标签表
    val labelDf = spark.read.csv("data/audience/label.csv").toDF("id", "label")
    labelDf.printSchema()
    labelDf.createOrReplaceTempView("label")
    val labelIdDF = spark.sql("select id from label where label = '吃货'")
    spark.catalog.dropTempView("label")

    val behaviorDf = spark.read.csv("data/audience/behavior.csv").toDF("id", "action")
    behaviorDf.printSchema()
    behaviorDf.createOrReplaceTempView("behavior")
    val behaviorIdDF = spark.sql("select id from behavior where action = '一个星期内购买10件衣服'")
    //标签为吃货的用户，且行为没有一个星期内购买10件衣服的用户，且主表要有这个用户
    //所以标签和行为求差集再和主表求交集
    //这里201700406002虽然是吃货，但是一个星期内购买10件衣服，所以不属于人群包
    val frame = labelIdDF.except(behaviorIdDF).intersect(masterIdDF)
    frame.show()
    //这里假设人群包id为随机数
    val audiencePackageId = Random.nextInt(100000);
    //frame.write.mode(SaveMode.Overwrite).parquet("hdfs://192.168.1.101:9000/user/bigdata/audience/" + audiencePackageId)
    frame.write.mode(SaveMode.Overwrite).parquet("data/user/bigdata/audience/" + audiencePackageId)

    spark.sqlContext.clearCache()
    spark.stop()
    spark.close()
  }

}
