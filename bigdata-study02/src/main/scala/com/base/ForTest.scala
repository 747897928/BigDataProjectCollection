package com.base

object ForTest {
  def main(args: Array[String]): Unit = {
    //for推导的格式
    //for(发生器){循环体}
    //发生器: 变量 <- 待遍历元素

    //构建一个Int类型的数组
    val arr=Array(18,0,9,38,100);
    //遍历数组，输出每个元素
    for(elem <- arr){
      println(elem)
    }
    println("-----------")
    //输出数组中的偶数元素
    for(i <- arr){
      if(i%2==0){
        println(i)
      }else{
        println("-1")
      }
    }
    //for+if守卫
    for(i <- arr if i%2==0 ){
//      if(i%2==0){
        println(i)
//      }
    }

    for(elem <- arr){
      for(j <- arr){
        println(elem+":"+j)
      }
      println("-----其他代码----")
    }

    for(i<-arr;j<-arr){
      println(s"$i:$j")
    }

    //编程语言的使用人数
    //val data1=1.to(20,2)
    val data1=Array(30000,5000,17000,2000,6000,15000)
    //编程语言的种类
    val data2=Array("java","scala","sql","c","c++","python")

    //输出编程语言的字符长度大于4的
    for(i <- data2 if  i.length()>4){
      println(i)
    }
    println("--------")
    //输出编程语言的字符长度大于4的且使用人数大于10000的
    for(i <- data2 if  i.length()>4; num <- data1 if num>10000){
      println(s"$i 的使用人数为$num")
    }
    println("-------")
    for(i <- data2;num <- data1 if i.length()>4 if num>10000){
      println(s"$i 的使用人数为$num")
    }


    for(i <- arr){
      println("打印")
    }
    //遍历数组arr中的每个元素，对元素+1之后重新保存到数组中
    val newarr=for(i <-arr) yield{
      i+1
    }

    val data:IndexedSeq[Int]=1 to 10
    var i=0;
    while(i<data.size){
      println(data(i))
      i+=1
    }







  }
}
