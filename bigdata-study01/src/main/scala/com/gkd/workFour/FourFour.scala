package com.gkd.workFour

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/23 15:19
 */
class FourFour {
   @Test
   def testFour(): Unit ={
     Logger.getLogger("org").setLevel(Level.ERROR)
     val conf = new SparkConf();
     conf.setMaster("local[*]").setAppName("MySparkTestFour");
     val sc = new SparkContext(conf);
     val data = sc.textFile("data/Data01.txt");
     val stdTuple = data.map(x => {
       val strings = x.split(",");
       (strings(0), strings(1));
     });
     stdTuple.mapValues(x => (x,1)).reduceByKey((x,y) =>(" ",x._2+y._2)).mapValues(x=> x._2).foreach(println)
   }
  @Test
  def testFive(): Unit ={
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf();
    conf.setMaster("local[*]").setAppName("MySparkTestFour");
    val sc = new SparkContext(conf);
    val data = sc.textFile("data/Data01.txt");
    val dataBaseList = data.filter(x=>x.split(",")(1)=="DataBase");
    println(dataBaseList.count())
  }
  @Test
  def testSix: Unit ={
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf();
    conf.setMaster("local[*]").setAppName("MySparkTestFour");
    val sc = new SparkContext(conf);
    val data = sc.textFile("data/Data01.txt");
    val courceList = data.map(x=>{
      val strings = x.split(",");
      (strings(1),strings(2).toInt);
    });
    val tuples = courceList.mapValues(x => (x, 1))
      .reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
      .mapValues(x => (x._1 / x._2)).collect();
    println(tuples);
  }

  @Test
  def testSeven(): Unit ={
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf();
    conf.setMaster("local[*]").setAppName("MySparkTestSeven");
    val sc = new SparkContext(conf);
    val data = sc.textFile("data/Data01.txt");
    val tmpdata = data.filter(x=>x.split(",")(1)=="DataBase")
      .map(x=>(x.split(",")(1),1))
    val accum = sc.longAccumulator("My Accumulator")
    tmpdata.values.foreach(x => accum.add(x))
    println(accum.value)
  }
  @Test
  def testTwoOne(): Unit ={
    import org.apache.spark.SparkContext
    import org.apache.spark.SparkContext._
    import org.apache.spark .SparkConf
    import org.apache.spark.HashPartitioner
    val conf = new SparkConf().setAppName(" testTwoOne").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val data = sc.textFile("data/test2.txt",2)
    val res = data.filter(_.trim().length>0).map(line=>(line.trim(),""))
      .partitionBy(new HashPartitioner(1))
      .groupByKey().sortByKey().keys
    res.saveAsTextFile("res")
  }
  @Test
  def testThreeOne(): Unit ={
    import org.apache.spark.SparkContext
    import org.apache.spark.SparkContext._
    import org.apache.spark .SparkConf
    import org.apache.spark.HashPartitioner
    val conf = new SparkConf().setAppName("testThreeOne").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val data = sc.textFile("data/test3.txt",3)
    val res = data.filter(_.trim().length>0)
      .map(line=>{
        val strings=line.split(" ");
        (strings(0).trim(), strings(1).trim().toInt)
      })
      .partitionBy(new HashPartitioner(1))
      .groupByKey().map(x=> {
      var n= 0
      var sum= 0.0
      for(i<-x._2) {
        sum = sum + i
        n = n + 1
      }
        val avg = sum/n
        (x._1,f"$avg%1.2f".toDouble)
     })
    res.saveAsTextFile("res2")
  }
}

