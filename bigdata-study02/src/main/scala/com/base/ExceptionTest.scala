package com.base

import java.io.IOException

object ExceptionTest {
  def main(args: Array[String]): Unit = {
    println(test(4,0))
  }

  def test(num1:Int,num2:Int)={
    try{
      num1/num2
    }catch {
      case e:IOException =>
        println("记录一下IOException.....")
      case e:ArithmeticException=>
        println("记录一下：除数为0，请检查除数的值！")
    }

  }

}
