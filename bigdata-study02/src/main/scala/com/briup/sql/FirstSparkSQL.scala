package com.briup.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.DataFrame

/**
  *第一SparkSQL程序
  * */
object FirstSparkSQL {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    import org.apache.spark.sql.SparkSession;

//    val conf=new SparkConf()
//    conf.setMaster("local[*]")
//    conf.setAppName("spark sql test..")

    val spark=SparkSession.builder()

      .master("local[*]")
//      .config("spark.master","local[*]")
      //  .config(conf)
      .appName("spark sql test")
      .getOrCreate();
    import spark.implicits._
    //在spark-sql中获取一个rdd
    val sc=spark.sparkContext;
    val conf=sc.getConf;
//    println(conf.get("spark.master"))
//
//    println(spark)
//    println("获取Spark的上下文对象:"+spark.sparkContext)
//    println("获取Spark的配置对象"+spark.sparkContext.getConf)
//
//    println("runtime conf="+spark.conf)

    //核心编码
    //1.获取DataFrame对象
    //1.1读取外部数据集 show databases; show tables;
    //数据没有格式
    //text ---> 1列 value
//    val df1=spark.read.text("data/test.txt")
    //val df1=spark.read.text("data/students.txt")
    val df1=spark.read
      .csv("data/students.txt")
      .toDF("stuId","stuName","age","score","className")
//    df1.show(false)
//    println(df1.count()+"条数据")
    //sc.textFile("data/test.txt").foreach(println)
    //2.将DataFrame注册为临时视图( 针对当前 SparkSession 会话级别)
    df1.createOrReplaceTempView("students")
    //( 针对所有 SparkSession 会话级别)
//    df1.createOrReplaceGlobalTempView("students")
    //3.通过SQL语句对数据进行分析处理
    val df2=spark.sql("select stuName,age " +
      " from students " +
      " where age > 21")
    df2.show()
    //4.1业务需求:统计每个班级的总分
    //4.2业务需求:统计每个班级的平均分

    //将Seq集合转化为DataFrame
    val seq=1 to 100
    val df3=seq.toDF()
    df3.show()
    val df4=seq.toDF("id")
    df4.show(10)

    val rdd1=sc.makeRDD(seq);
    val df5: DataFrame =rdd1.toDF("test")
    df5.show(3)








    spark.stop();
  }
}
