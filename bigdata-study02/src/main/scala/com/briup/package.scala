package com

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Spark使用工具类
  * 方便构建使用Spark
  * */
package object briup {

  Logger.getLogger("org").setLevel(Level.WARN);

  private var _conf:Option[SparkConf]=None;
  private var _sc:Option[SparkContext]=None;
  private var _spark:Option[SparkSession]=None;
  private var _ssc:Option[StreamingContext]=None;
  implicit val jarFilePath:Option[String]=None;

  private def getConf(master:String,appName:String,checkPoint:String="spark-checkpoint"):SparkConf={
    _conf match{
      case Some(conf) => conf
      case None =>
        val conf=new SparkConf()
        conf.setMaster(master)
        conf.setAppName(appName)
        conf.set("spark.sql.streaming.checkpointLocation",checkPoint)
        _conf=Some(conf)
        conf
    }
  }
  /**
    * 构建一个工具方法，使用该方法便捷的获取SparkContext对象
    * */
  def getSparkContext(master:String,appName:String)(implicit jarFilePath:Option[String]=None):SparkContext={
    _sc match{
      case Some(sc) => sc
      case None =>
        val conf=getConf(master,appName)
        //第一种构建方式
        //    val sc=new SparkContext(conf);
        //第二种构建方式
        val sc=SparkContext.getOrCreate(conf);
        jarFilePath match {
          case Some(filepath) => sc.addJar(filepath)
          case None =>
        }
        _sc=Some(sc)
        sc
    }
  }
  /**
    * 构建一个工具方法，使用该方法便捷的获取SparkSession对象
    * */
  def getSpark(master:String,appName:String,checkPoint:String="spark-checkpoint")(implicit jarFilePath:Option[String]):SparkSession={
    _spark match{
      case Some(spark) =>
        //        println("...获取已经存在的Spark...")
        spark
      case None =>
        //        println("...开始创建Spark...")
        val conf=getConf(master,appName)
        val spark=SparkSession.builder().config(conf).getOrCreate();
        jarFilePath match {
          case Some(filepath) => spark.sparkContext.addJar(filepath)
          case None => //println("无jarFilePath......");
        }
        _spark=Some(spark)
        spark
    }
  }
  def getSpark(ssc:StreamingContext)(implicit  jarFilePath:Option[String]):SparkSession={
    val sc=ssc.sparkContext;
    getSpark(sc.master,sc.appName)(jarFilePath)
  }
  /**
    * 构建一个工具方法，使用该方法便捷的获取StreamingContext对象
    * */
  def getStreamingSpark(master:String,appName:String,batchDur:Duration)(implicit jarFilePath:Option[String]=None):StreamingContext={
    _ssc match{
      case Some(ssc) =>ssc
      case None =>
        val conf=getConf(master,appName)
        val ssc=new StreamingContext(conf,batchDur)
        jarFilePath match {
          case Some(filepath) => ssc.sparkContext.addJar(filepath)
          case None =>  //println("无jarFilePath......");
        }
        _ssc=Some(ssc)
        ssc
    }
  }
}

