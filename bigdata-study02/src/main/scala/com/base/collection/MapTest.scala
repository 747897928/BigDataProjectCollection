package com.base.collection

import org.junit.Test

/**
  * Map集合测试类
  * */
class MapTest {

  @Test
  def zipTest(): Unit ={
    val data1=Array("<","-",">")
    val data2=Array(2,10)

    val zip1: Array[(String, Int)] =data1.zip(data2);
    zip1.foreach(println)

    zip1.map( x => { x._1*x._2 } ).foreach(print)

    data1.zipAll(data2,"*",8).foreach(println)

    data1.zipWithIndex.foreach(println)

    val result1: (Array[String], Array[Int]) =zip1.unzip;
    val arr3=Array((1,"lily",18),(2,"tom",19),(3,"john",20))
    val result2: (Array[Int], Array[String], Array[Int]) =arr3.unzip3;


  }

  //遍历Map集合
  @Test
  def foreachMap(): Unit ={
    import scala.collection.mutable;
    val map:mutable.Map[String,Int]=mutable.Map.empty[String,Int];
    val str: String ="scala java scala r sql scala r";
    //统计这个字符串中每个单词出现的频次
    val arr: Array[String] =str.split(" ")
//    for(word <- arr){
//      //判断map集合中是否有该单词，
//      if(map.contains(word)){
//        //如果有该单词，取出对应的value值，+1之后重新赋值给map集合
//        val value=map.apply(word)+1
//        //添加到Map集合
//        map.+=(word->value)
//      }else{
//        //单词不存在，说明该单词第一次出现
//        //直接将该单词作为key,数字1作为value值
//        //添加到Map集合
//        map.+=(word->1)
//      }
//    }
//    for(word <- arr){
//      val value=map.getOrElse(word,0)+1
//      map.+=(word->value)
//    }
    arr.foreach(word=>{
      map.+=(word->(map.getOrElse(word,0)+1))
    })
    //查看集合中的词频次数
    map.foreach(println)
    for(elem <- map){
      println(s"key=${elem._1},value=${elem._2}")
    }

    for( (key,value) <- map){
      println(s"key=$key,value=$value")
    }

    map.keys.foreach(key=>println(s"key=$key"))
    val a=map.valuesIterator
//    val a=map.values
    a.foreach(println)
    println("---------")
    a.foreach(println)


  }

  @Test
  def testMap(): Unit ={
    //统一对象构建原则
    //不可变的Map集合
    val map1=Map.empty[String,Int]

    //可变的Map集合
    import scala.collection.mutable
    val map2=mutable.Map.empty[Int,Char]

    val map3=Map.apply(("bye",5),("hi",10))
    val map4=Map(("bye",5),("hi",10))
    val map5: Map[String, Int] =Map("bye"->5,"hi"->10)

    //往不可变集合中添加/移除元素
//    map5.+("hello"->10).foreach(println)
//    map5.++:(Map("hello"->10,"java"->9)).foreach(println)
//    map5.-("bye").foreach(println)
    //往可变集合中添加/移除元素
    //map2.+(10->'A').foreach(println)
    map2.+=(10->'A').foreach(println)
    map2.++=(Map(1->'A',2->'B',3->'C')).foreach(println)
    val resultValue=map2.remove(11)
    println(resultValue)
    map2.foreach(println)

    //根据key获取Value值
//    println(map2.apply(2))//B
//    println(map2.apply(11))//???

    println(map2.get(2))
    val result=map2.get(11);
    if(!result.isEmpty){
      val value=result.get
      println("value="+value)
    }else{
      println("value=0")
    }
    println(map2.get(11))

    val v=map2.getOrElse(11,'Z')
    println(s"v=$v")

  }



}
