package com.base.objectT

abstract class Father {
  //抽象属性
  val name:String
  //具体属性 public  age age_=
  var age=0
  //抽象方法
  //abstract
  def methodName():Int
  //具体方法
  def test(args:String):Int={
    args.length
  }
}
class Son extends Father{
  //子类重写 父类中的抽象方法
  //override
  def methodName():Int={
    3
  }
  //子类重写 父类中的抽象属性
  //override
  val name:String="tom"
  //子类重写 父类中的具体方法
  override
  def test(args:String):Int={
    args.length+1
  }
  //子类重写父类中的具体属性age 修改age的值
  //override var age=10

}

class Month{
  val num = 31 // def num
  val days = new Array[Int](num) //def days
}
class Week(override val num:Int) extends Month{
  // 1个私有的属性num 1个公有的num(重写)
}

object  Father{
  def main(args: Array[String]): Unit = {
    val a=new Week(7)
    println(a.days.length)
  }
}

