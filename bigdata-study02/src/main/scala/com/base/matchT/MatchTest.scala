package com.base.matchT

import com.base.Book

object MatchTest {
  def main(args: Array[String]): Unit = {
    println(match1("hi"))
    println(match1("3"))

    val result1=3 match {
      case 1 => "hello"
      case 2 => new Book("01","java",2.4)
      case _ => "other info"
    }

    val num=3
    val result2=num match {
      case 1 =>
        println("test...")
        println("test...")
        1
      case x =>
        println(s"x=$x")
        x*2;
    }
    println(result2)

    val list=List(10,20,30,40,50,60,70,100)
    list match {
      case List(a,b,c,d) =>
        println(s"a=$a,d=$d")
      case List(a,b,_) =>
        println(s"a=$a,b=$b")
      case List(a,_*) =>
        println(s"a=$a")
      case _ =>
        println("other...")
    }

    printType(3.14)

    //子类类型转化为父类类型 隐式转化
    //Animal a=new Cat();
    //a Animal Cat
    //if(a  instanceof Cat){
      //父类类型转化为子类类型 强制类型转化
      //Cat c=(Cat)a;
    //}

    val a:Animal=new Animal;
    val c:Cat=new Cat;
    val d:Dog=new Dog;

    //val a1:Animal=c;
    val a1:Animal=c;

    if(a1.isInstanceOf[Cat]){
      val c1:Cat=a1.asInstanceOf[Cat];
      println(c1)
    }

    a1 match {
      case x:Cat => println(x)
      case _ => println("----")
    }


    testType(List("a"))

  }

  def testType(list:List[Any]): Unit ={
    list match {
      case n:List[_] if n.length>3 =>
        println("this is _")
//      case n:List[String] =>
//        println("this is strings")
    }
  }

  def printType(x:Any)={
    x match{
      case s:String => println(s"$s 的类型为String")
      case i:Int => println(s"$i 的类型为Int")
      case other => println("other type ")
    }
  }

  def match1(x:Any)={
    x match{
      case "hi" => "hello"
      case  3 => "three"
      case true  => "truth"
      case Nil => "this is empty list"
      case _ => "other info"
    }
  }
}
