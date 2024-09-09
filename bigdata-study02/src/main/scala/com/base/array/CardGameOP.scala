package com.base.array
import scala.collection.mutable.ArrayBuffer

object CardGameOP {
  def main(args: Array[String]): Unit = {
    //选择什么容器 Array ArrayBuffer
    //1.将四种花色存放到容器数组中
    val colors=(9824 to 9827).map( _.toChar )
    //2.将2～10 J Q K A 存放到牌面数值容器中
    val cardNums=2.to(10).map(_.toString)
      .++(Set("J","Q","K","A"))
      .toBuffer
    //3.遍历拼接  数值+花色 新的容器(2♥️，2♠️,2♦️,2♣️)
    val cards =cardNums.flatMap(num =>{ colors.map(color=> { num+color } )  })
//      .flatten
    //4.需要将大小王添加进去 joker Joker
    cards.append("joker")
    cards.append("Joker")
    //5.需要获取玩家个数
    println("请输入玩家人数：")
    val nplayers=scala.io.StdIn.readInt();

    //6.根据玩家个数创建对应个容器对象，分别保存对应的扑克
    // [   A [... ]   B  [ ... ]  C [ ... ]  ]
    val players=new Array[ArrayBuffer[String]](nplayers)
    println("长度："+players.size)
    //需要对二维数组进行初始化操作
    0.until(nplayers).foreach(i=>{
      players(i)=new ArrayBuffer[String]()
    })

    //7.从cards中分发扑克
    (0 until cards.length-nplayers).foreach{i => {
      //借助于随机数
      val cardIndex=scala.util.Random.nextInt(cards.length)
      //往用户的扑克容器中添加新的扑克
      players(i%nplayers).append(cards(cardIndex))
      //从cards中移除该扑克
      cards.remove(cardIndex)
    } }

    //8.查看每个玩家的扑克集合
    println("每个玩家的扑克牌：")
    players.foreach(println)
    println("地主牌：")
    cards.foreach(println)

  }

}
