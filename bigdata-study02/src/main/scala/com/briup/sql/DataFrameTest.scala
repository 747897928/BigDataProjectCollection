package com.briup.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.junit.Test

class DataFrameTest {
  val spark=SparkSession.builder()
    .master("local[*]")
    .appName("dataframe create")
    //.enableHiveSupport()
    .getOrCreate()
  import spark.implicits._
  //println(spark)
  val sc=spark.sparkContext

  @Test
  def test1(): Unit ={
    val seq=1 to 100
    val rdd: RDD[Int] =sc.makeRDD(seq);
    //将RDD转化为DataFrame对象
    val df1=rdd.toDF();

    val bookSeq:Seq[Book] =Seq(new Book(1,"Java编程",49.8)
        ,Book(2,"Scala编程",67.3))
    val bookRDD:RDD[Book]=sc.parallelize(bookSeq)

    //样例类是Procuct的子类
    val df2: DataFrame =spark.createDataFrame(bookRDD)
//    val df2: Dataset[Row] =spark.createDataFrame(bookRDD)
    val df3=spark.createDataFrame(bookSeq);

    val row1=Row(1,"Java编程",49.8)
    val row2=Row(2,"Scala编程",67.3)
    val rowSeq=Seq(row1,row2)
    val rowRDD: RDD[Row] =sc.makeRDD(rowSeq)
    import org.apache.spark.sql.types._
    val schema=StructType(Array(
      StructField("id",IntegerType),
      StructField("name",StringType),
      StructField("prices",DoubleType)))
    val schema1=new StructType()
      .add(StructField("id",IntegerType))
      .add("name",StringType)
      .add("price",DoubleType)
    val df4=spark.createDataFrame(rowRDD,schema = schema)







  }
}
