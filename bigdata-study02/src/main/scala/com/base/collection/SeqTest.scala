package com.base.collection

import org.junit.Test

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class SeqTest {
  @Test
  def testIndexedSeq(): Unit ={
    //Seq不可变集合
    val seq1=Seq.empty[Int];
    val seq2=IndexedSeq.empty[Int];

    //Vector Range
    val seq3=Vector.empty[Int];
    val newSeq3=seq3.+:(1)

    val seq4=Range.apply(1,10,3)
    val seq8=new Range(1,10,2)
    val seq6=1.to(10)
    val seq7=1.until(end = 10,step = 3)

    val seq5=ArrayBuffer.empty[Int]
    seq5.+=(1);

    val list1=List(1,2,3,4,5)
    println("和："+sum(list1))

  }

  def sum(list:List[Int]):Int={
    if(list==Nil)
      0
    else
      // head tail
      list.head+sum(list.tail)
  }
}
