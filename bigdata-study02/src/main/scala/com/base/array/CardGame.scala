package com.base.array

import scala.collection.mutable.ArrayBuffer

/**
  * (斗地主)扑克牌发牌器 54张牌
  * 花色  四种 9824~9827 对应的字符值是否是花色
  * 牌面大小 2-10 J Q K A
  * 大小王 joker Joker
  * */
object CardGame {

  def main(args: Array[String]): Unit = {
    //如何将数字转化为字符类型
//    println(9824.toChar)
//    println(9825.toChar)
//    println(9826.toChar)
//    println(9827.toChar)

    //选择什么容器 Array ArrayBuffer
    //1.将四种花色存放到容器数组中
    val colors=new Array[Char](4);
    colors(0)=9824.toChar;
    colors(1)=9825.toChar;
    colors(2)=9826.toChar;
    colors(3)=9827.toChar;

    //2.将2～10 J Q K A 存放到牌面数值容器中
    val cardNums=ArrayBuffer("2","3","4","5",
      "6","7","8","9","10","J","Q","K","A")
    //3.遍历拼接  数值+花色 新的容器(2♥️，2♠️,2♦️,2♣️)
    val cards=for(num <- cardNums; color <- colors ) yield{
      num+color
    }
    //4.需要将大小王添加进去 joker Joker
    cards.append("joker")
    cards.append("Joker")
//    cards.foreach(println)

    //5.需要获取玩家个数
    println("请输入玩家人数：")
    val nplayers=scala.io.StdIn.readInt();

    //6.根据玩家个数创建对应个容器对象，分别保存对应的扑克
    // [   A [... ]   B  [ ... ]  C [ ... ]  ]
    val players=new ArrayBuffer[ArrayBuffer[String]]()
    println("长度："+players.size)
    //需要对二维数组进行初始化操作
    for(index <- 0 until nplayers){
      //players(index)=new ArrayBuffer[String]()
      players.append(new ArrayBuffer[String]())
    }

    //7.从cards中分发扑克
    for(i <- 0 until cards.length){
      //借助于随机数
      val cardIndex=scala.util.Random.nextInt(cards.length)
      //往用户的扑克容器中添加新的扑克
      players(i%nplayers).append(cards(cardIndex))
      //从cards中移除该扑克
      cards.remove(cardIndex)
    }

    //8.查看每个玩家的扑克集合
//    players.foreach(player=>{
//      player.foreach(print)
//      println()
//    })
    players.foreach(println)








  }

}
