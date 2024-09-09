package com.base

//实体类 Ordered
class Book(var bookId:String
           ,var name:String
           ,var price:Double)
  //extends Comparable[Book]
extends Ordered[Book]
{
  //重写的Ordered特质中的抽象方法
  override def compare(that: Book): Int = {
    this.name.compare(that.name)
  }

//  override def compareTo(o: Book): Int = {
//    //排序规则
//    //根据书本名字进行比较
//    //两个book对象  o this
//    val oname=o.name;
//    val name=this.name;
//    - name.compareTo(oname)
//  }

  //重写toString方法
  override def toString: String = {
    s"Book[bookId=$bookId,name=$name,price=$price]"
  }

}

