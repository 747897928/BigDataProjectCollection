package com.base.matchT

object FindTest {
  def main(args: Array[String]): Unit = {
    val name="kevin"
    val list=List("larry","kevin","terry")
    println(find(name,list))
    new Array[Int](1);
  }

  def find(name:String,list:List[String]):Option[Int]={
    var result=Option.empty[Int];
    //遍历list集合，判断name是否在集合中，如果在，返回下标
    import scala.util.control.Breaks._
    breakable{
      (0 until list.length).foreach(index=>{
        if(list(index)==name){
          result=Some(index)
          break;
        }
      })
    }

    result
  }
}
