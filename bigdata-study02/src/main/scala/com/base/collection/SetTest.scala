package com.base.collection

import org.junit.Test

import scala.collection.immutable.{HashSet, SortedSet, TreeSet}
import scala.collection.mutable

class SetTest {
  @Test
  def defineSet()={
    //1.Set SortedSet  HashSet TreeSet BitSet
    //LinkedHashSet
    //未知类型
    val set1=Set.empty[Int]
    //TreeSet
    val set2=SortedSet.empty[String]
    val set3=TreeSet.empty[String]
    val set4=HashSet.empty[Int]
    import scala.collection.immutable.BitSet
    val set5=BitSet.empty

    //可变版本
    val setm1=mutable.LinkedHashSet.empty[String];
    val setm2=mutable.BitSet.empty

    setm2.+=(1,2,3)

    val setm3=Set(1,4,5)

    //union 合集 | ++
    setm2.union(setm3).foreach(println)
    println("-----")
    //diff 差集 &~ --
    setm2.diff(setm3).foreach(println)
    println("-----")
    //intersect 交集 &
    setm2.intersect(setm3).foreach(println)

    println(setm2.subsetOf(setm3))

    /*setm2.collect{ case x => x }
    setm2.foreach(x=>println(x))*/

    //set1.reduce()






  }
}
