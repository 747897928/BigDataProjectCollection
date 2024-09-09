package com.base

import org.junit.Test

class BreakTest {

  //Junit单元测试
  //break功能
  @Test
  def test_break()={
    println("junit test ")
    val flag=getNum()
    //1.导入break相关方法的支持
    import scala.util.control.Breaks._
    breakable(
      for(elem <- 1 to 10){
        if(elem==flag){
          break
        }
        println(s"elem:$elem")
      }
    )
    //.....
  }
  //模拟的用户输入
  def getNum():Int={
    4
  }

  @Test
  def test_continue(): Unit ={
    for(i <-  0 until 10){
      import scala.util.control.Breaks._
      breakable {
        if (i == 3 || i == 6)
          break()
        println(s"i=$i")
      }
    }
  }

  @Test
  def testPackage(): Unit ={
    test()
    println(defaultName)
  }
}
