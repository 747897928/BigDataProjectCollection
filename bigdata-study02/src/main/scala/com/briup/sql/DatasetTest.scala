package com.briup.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.junit.Test

class DatasetTest {
  Logger.getLogger("org").setLevel(Level.ERROR)
  val spark=SparkSession.builder()
    .master("local[*]")
    .appName("dataset create")
    .getOrCreate()
  import spark.implicits._
  val sc=spark.sparkContext
  @Test
  def createDataset(): Unit ={
    //1.获取seq以及rdd对象
    val seq=1 to 100
    val rdd1=sc.makeRDD(seq)

    //2.1将seq或rdd转化为dataset
    val ds1: Dataset[Int] =seq.toDS()
    val ds2: Dataset[Int] =rdd1.toDS()

    //2.2
    val ds3: Dataset[Int] =spark.createDataset(seq);
    val ds4: Dataset[Int] =spark.createDataset(rdd1);

    //2.3将DataFrame转化为Dataset
    val df1: DataFrame =spark.read.json("data/students.json")
    df1.show()
    df1.printSchema()
    //
    val ds5: Dataset[Student] =df1.as[Student]
    ds5.show()
    df1.map(row=>row.getString(2))
    df1.filter(row =>row.getLong(0)>21).map(row=>row.getString(4)).show()
    ds5.filter(stu=>stu.age>21).map(stu=>stu.stuName).show()

//    ds1.show()
//    ds2.show()
//    ds3.show()
//    ds4.show()

  }

  @Test
  def testBaby(): Unit ={
    val fileRDDs = sc.wholeTextFiles("data/ssa_names/yob*.txt");
    val babys = fileRDDs.flatMap {
      case (filePath, fileInfo) => {
        val yearInfo = filePath.substring(filePath.lastIndexOf("/")).substring(4, 8);
        //每年同名同性别孩子的个数 一行数据
        val arr = fileInfo.split("\\n");
        //将数据封装成对象
        val userMap = arr.map(line => {
          val Array(name, gender, count) = line.split(",");
          (yearInfo, name, gender, count.trim.toLong);
        });
        userMap
      }
    }.toDS();
    babys.show()
  }

  @Test
  def testBaby1(): Unit ={
    val fileRDDs = sc.wholeTextFiles("/Users/angelia/spark/ssa_names/yob*.txt");
    val babys= fileRDDs.flatMap {
      x => {
        val yearInfo = x._1.substring(x._1.lastIndexOf("/")).substring(4, 8);
        //每年同名同性别孩子的个数 一行数据
        val arr = x._2.split("\\n")
        //将数据封装成对象
        val userMap= arr.map(line => {
          val Array(name, gender, count) = line.split(",");
          (yearInfo,name,gender,count.trim)
        });
        userMap
      }
    }.toDS();
    babys.show()
  }


}
