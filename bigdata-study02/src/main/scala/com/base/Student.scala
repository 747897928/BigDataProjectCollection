package com.base

import scala.beans.BeanProperty

class Student {
  //1.属性
  var name:String=_
  var age:Int=_
  @BeanProperty var gender:String=_
  //2.方法
  def show()={
    println("show...s")
  }
  def main(args: Array[String]): Unit = {
    println("hello,scala!")
  }
}
