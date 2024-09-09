package com.base
//默认导入了三个包
//import java.lang._
//import scala._
//import scala.Predef._
object HelloScala {
  //object Predef{都是静态的}
  //public static void main(String[] args){
  // System.out.println("hello,java");
  // }
  //程序入口 静态方法
  def main(args: Array[String]): Unit = {
    println("hello,scala")
    Predef.println("hello,scala!")
    scala.Predef.println("......")
    scala.io.StdIn
  }
}
