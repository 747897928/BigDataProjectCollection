package com.gkd.workOne

/**
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/9 15:06
 */
import scala.io.StdIn.readDouble
object ExpTest {
  def main(args: Array[String]): Unit = {
    println("请输入底数x");
    var x=readDouble();
    println("请输入项数n");
    var n=readDouble();
    var sum=powSum(x, n);
    var n_1=n+1;
    print(s"级数前$n_1 项之和为：$sum")
  }

  def powSum(x: Double, n: Double): Double = {
    if (n <= 0) 1;
    var multiply = x;
    multiply = math.pow(x, n)
    if (n - 1 < 0) 1;
    else multiply + powSum(x, n - 1);
  }
}
