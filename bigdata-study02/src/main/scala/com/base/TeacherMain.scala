package com.base

import java.util

//1.自己写程序入口
object TeacherMain extends App {
//  def main(args: Array[String]): Unit = {
//    //写自己的代码
//  }
//  println("hello,APP")

  //1.创建老师对象 最初版本 上线之后 评分
  val tea=new Teacher(name="larry",age=60)
  tea.work("上网课！")
  tea work "上网课"

  //2.如果我希望老师在work前后做一些事情(Logger记录)

  //特质可以混入到实例对象
  val tea1=new Teacher("larry",age=60) with Logger {
    override def error(info: String): Unit = {
      println("teacher1......error:")
    }
  }
  tea1.debug("调试直播设备")
  tea1.work("上网课！")
  tea1.info("在线答疑！")

  type Hj=com.base.Hjdhikfalakdkkl
  val hj=new Hj();

  //空的Map集合
  import scala.collection.immutable.Map
  val map1=Map.empty[String,Int];

  //构建一个Java的HashMap对象
  //HashMap<String,Int> map=new HashMap<>();
  val map2:util.HashMap[String,Int]=new util.HashMap[String,Int]()
  //....进行一些操作

  //构建Scala的HashMap对象
  val map3:scala.collection.mutable.HashMap[String,Int]=
    new scala.collection.mutable.HashMap[String,Int]()

  type JavaHashMap[K,V]=java.util.HashMap[K,V]
  type ScalaHashMap[K,V]=scala.collection.mutable.HashMap[K,V]

  val javamap=new JavaHashMap[String,Int]();
  val scalamap=new ScalaHashMap[String,Int]();

  val abc123=10;
  val hj$=10;
  println(hj$)

  val `val`=20;




}
