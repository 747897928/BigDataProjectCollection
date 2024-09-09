package com.base.array

import org.junit.Test

/**
  * 不可变数组测试类
  * */
class ArrayTest {

  @Test
  def defineArray(): Unit ={
    //1.定义不可变数组
    //1.1借助与类的构造器创建
    val arr1: Array[String] =new Array[String](10)
    //1.2借助于对象的静态方法创建对象 统一对象构建原则
    val arr2: Array[String] =Array.apply("hello","scala","bye")
    val arr3: Array[String] =Array("hello","scala","bye")
    // Set Map  Seq
//    val set: Set[Int] =Set(1,2,3,4,5);
//    val seq:Seq[Char]=Seq('A','B','C');
//    val map=Map((1,"C"),(2,"java"),(3,"scala"))

    //2.给arr1赋值
    arr1(0)="hello"
    arr1(9)="bye"
    arr1.update(1,"张无忌")
    //3.从arr1中取值
    println(s"0=${arr1(0)},1=${arr1(1)}")
    val elem1=arr1.apply(0)
    val elem2=arr1(0)
    println(arr1.size)
    arr1.take(3).foreach(println)
    println(arr1.takeRight(2))


    //3.遍历arr2数组
    for(elem <- arr2){
      println(elem)
    }
    for( i <- 0 until  arr2.length){
      println("::"+arr2.apply(i))
      println("$$"+arr2(i))
    }
    arr2.foreach(println)

    arr2.foreach(  x => { println("前缀:"+x);println("---")  } )


  }

}
