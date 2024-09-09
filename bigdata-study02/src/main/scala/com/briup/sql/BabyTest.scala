package com.briup.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.junit.Test

class BabyTest {
  new SparkConf()
  Logger.getLogger("org").setLevel(Level.ERROR)
  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("全美婴儿分析案例")
    .getOrCreate()

  import spark.implicits._

  val sc = spark.sparkContext

  @Test
  def test1(): Unit = {
    //1.130年中每年出生婴儿的男女比例
    //1.1读取数据，加载为RDD/Dataset
    //val df=spark.read.csv("data/ssa_names/yob*.txt")
    //df.show()
    // 1690784 2000 10300 33838
    //println(df.count())

    //RDD[(文件绝对路径名,文件中的全部内容)]
    val rdd: RDD[(String, String)] = sc.wholeTextFiles("data/ssa_names/yob*.txt")
    //    println(rdd.count()) // 131
    //    rdd.foreach(line=>{
    //      println("文件名:"+line._1)
    //    })
    //(年份,(男人数,女人数))
    val rdd2 = rdd.flatMap(x => {
      //1.获取文件绝对路径名中的年份信息
      val path = x._1;
      val year = path.substring(path.lastIndexOf("/") + 4)
        .split("\\.")(0)
      //      println(year)
      //2.按照换行符分割文件中的数据   \\n   \\r  \\t
      val context = x._2;
      val count: Array[String] = context.split("\\n")
      val newArr = count.map(line => {
        val Array(name, gender, num) = line.trim.split(",")
        (year.toLong, name, gender, num.toLong)
      });
      newArr
    })
    val df1 = rdd2.toDF("year", "name", "gender", "num")
    //    df1.show(truncate = false)
    //计算每年的男女人数 按照年份和性别分组  求人数的总和
    df1.createOrReplaceTempView("babys")
    val sql = "select year,gender,sum(num) totalNum from babys group by year,gender"
    val result1 = spark.sql(sql)
    //    result1.sort("year").show(131*2)


    val ds = df1.as[(Long, String, String, Long)]
    val ds2 = ds.groupBy("year", "gender")
      .sum("num")
    ds2.sort("year").show()

    ds.select($"year", $"name", $"gender", $"num")
      .limit(5)
      .sort($"year".desc)
      .show()

    ds.selectExpr("name", "num*2")
      .where("num>10000")
      .limit(10)
      .show()
    import org.apache.spark.sql.functions._
    ds.select($"name", expr("num*2").alias("numDouble"))
      .show(10)


    spark.stop()
  }

}
