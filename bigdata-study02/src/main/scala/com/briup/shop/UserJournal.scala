package com.briup.shop

import org.apache.log4j.{Level, Logger}
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.spark.sql.{Dataset, SparkSession}
import org.junit.Test

/**
 * <p>description: </p>
 * <p>create: 2021/2/10 10:25 </p>
 *
 * @author :zhaoyijie
 */
class UserJournal {

  Logger.getLogger("org").setLevel(Level.ERROR)
  //1.创建SparkSession对象
  val spark = SparkSession.builder()
    .appName("UserJournal")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  val sc = spark.sparkContext
  val df = sc.hadoopFile("H:/实训三/简历/毕设/SogouQ.reduced"
    , classOf[TextInputFormat], classOf[LongWritable], classOf[Text], 1)
    .map(p => new String(p._2.getBytes, "GBK"))
    .map(p => p.split("\t"))
    .map(p => (p(0), p(1), p(2).substring(1, p(2).length - 1), p(4)))
    .toDF("accessTime", "userId", "searchKeyword", "url")
  df.createOrReplaceTempView("userLogTable")

  @Test
  def test0: Unit = {
    val ds: Dataset[UserLog] = df.as[UserLog]
    ds.show(10, false)
    spark.sql("select accessTime,count(userId) as userCount," +
      "count(searchKeyword) as keyWordCount" +
      " from userLogTable group by accessTime,userId,searchKeyword").show(false)
  }

  @Test
  def test1(): Unit = {
    spark.sql("select accessTime,userId,count(userId) as userCount from userLogTable group by accessTime,userId order by userCount desc").show(false)
  }

  @Test
  def test2(): Unit = {
    spark.sql("select accessTime,searchKeyword,count(searchKeyword) as keyWordCount from userLogTable group by accessTime,searchKeyword order by keyWordCount desc").show(false)
  }

  @Test
  def test3(): Unit = {
    spark.sql("select searchKeyword,count(searchKeyword) as keyWordCount from userLogTable group by searchKeyword order by keyWordCount desc").show(false)
  }

}

case class UserLog(userId: String, accessTime: String, searchKeyword: String, url: String)
