package com.base

class T{
  def show()={
    println("test...")
  }
}
trait T1
trait T2
//特质
trait Animal extends T with T1 with  T2{
  //具体属性
  var name:String=_;
  //抽象属性
  var age:Int;
  //具体方法
  def sleep(num:Int)={
    //...
  }
  //抽象方法
  def eat(food:String):Unit;
}
//类扩展特质 需要重写特质中的抽象内容
class Cat extends  Animal{
  override var age: Int = 10

  override def eat(food: String): Unit = println("猫喜欢吃"+food)
}
//对象扩展特质 需要重写特质中的抽象内容
object Dog extends Animal{
  override var age: Int = _

  override def eat(food: String): Unit = ???
}

class Teacher(name:String,age:Int){
  def work(info:String): Unit ={
    println("老师工作："+info)
  }

}
//日志记录特质 log4j
trait  Logger{
  def error(info:String):Unit
  def info(info:String)={
    println("log:"+info)
  }
  def warn(info:String)={
    println("warn:"+info)
  }
  def debug(info:String)={
    println("debug:"+info)
  }
}

