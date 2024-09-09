package com.base.objectT

class Emp(name:String,salary:Double)
object Emp{
  def apply(name: String, salary: Double): Emp =
    new Emp(name, salary)

  def apply():Emp=
    new Emp("defaultName",0.0)
}


object AccountTest {
  def main(args: Array[String]): Unit = {
    //1.构建一个对象
    val account=new
        Account(name = "张三",amount =10000 )
    println(account.name)
    println(account.amount)
    val account2=Account.apply("李四",2000)

    val acc=Account(name="李四",amount = 1.0);
    println(acc.toString)

    val emp=new Emp("lily",salary =100)
    val emp2=Emp("lily",100)

  }
}
