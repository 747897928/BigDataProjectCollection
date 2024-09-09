package com.briup.sql

import org.apache.log4j.{Level, Logger}

object DataFrameExamples {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    import org.apache.spark.sql.SparkSession;

    val spark=SparkSession.builder()
      .master("local[*]")
      .appName("spark sql test")
      .getOrCreate();
    import spark.implicits._
    //核心编码
    //1.获取DataFrame对象
    val df1=spark.read
      .csv("data/students.txt")
      .toDF("stuId","stuName","age","score","className")
    df1.write.json("data/out/students_json")
//    spark.read.json("data/out/students_json")
    //2.将DataFrame注册为临时视图( 针对当前 SparkSession 会话级别)
    df1.createOrReplaceTempView("students")
    //3.通过SQL语句对数据进行分析处理
    val df2=spark.sql("select stuName,age " +
      " from students " +
      " where age > 21")
    df2.show()
    //将结果保存到文件中
    //df2.write.csv("data/out/students_csv")


    //4.1业务需求:统计每个班级的总分
    // select className,sum(score)  from students group by className
    spark.sql("select className,sum(score) totalScore  from students group by className").show()
    //4.2业务需求:统计每个班级的平均分
    spark.sql("select className,avg(score) avgScore from students group by className").show()

    spark.stop();
  }
}
