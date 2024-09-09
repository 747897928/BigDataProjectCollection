package com.base.array

import com.base.Book
import org.junit.Test

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class ArrayBufferTest {

  @Test
  def testBook(): Unit ={
    //1.构建一个可变数组，存放三个book对象
//    val books=new ArrayBuffer[Book]();
//    val books: ArrayBuffer[Book]=ArrayBuffer[Book]();
    val books=ArrayBuffer.empty[Book];

    val book1=new Book("202001","d快学Scala",46.8)
    val book2=new Book(bookId = "202002",name="a疯狂Java",price = 46.8)
    val book3=new Book(bookId="202003",name="b设计模式",price = 96.3)

    books.append(book1)
    books.append(book2)
    books.append(book3)

    //2.对可变数组的book对象进行排序
    // sorted sortBy sortWith
    //Comparable Comparator
//    books.sorted.foreach(println)
    books.sortBy(  x => (x.price,x.bookId) ).foreach(println)
    //books.sortWith( (b1,b2) => b1.price>b2.price )
    //books.sortWith(_.price>_.price).foreach(println)

  }

  @Test
  def bufferOtherOP(): Unit ={
    val buffer=ArrayBuffer("红楼梦","西游记4","水浒传","三国演义");
    //1.count方法
//    val result=buffer.count( x  => x.length>3 )
    val result=buffer.count( x  => true )
    println(result)

    //集合可以转化为可变数组 借助于toBuffer方法
    val nums=(1 to 10).toBuffer
    println("和："+nums.sum)
    println("乘积："+nums.product)
    println("最大值:"+nums.max)
    println("最小值:"+nums.min)

    val numbers=ArrayBuffer(19,12,15,2,29,100);
    val newBuffer=numbers.sorted
//    newBuffer.foreach(println)
    //将数字按照字符串比较规则进行比较
    numbers.sortBy( x => x.toString ).foreach(println)

//    buffer.sorted.foreach(println)
    //san shui hong xi

    //测试map filter
    val result1=for(elem <- numbers if elem%2==0)
                  yield elem*2
    println(result1)

    //Scala函数的字面量定义为 (参数列表) => {函数体}
    //函数的类型表示为 (参数类型) => 返回值类型
    val func1= (x:Int) => {x*2}
    numbers.map(func1)
    val newArray=numbers.map( (x:Int) => {x*2} )
    newArray.foreach(println)

    val func2= (x:Int) =>  {x%2==0}
    numbers.filter(func2).foreach(println)

    println("----")
    numbers.filter(  (x:Int) => { x%2==0 } )
      .map( (x:Int)=>{x*2} )
      .foreach(println)

    numbers.filter( x => x%2==0  )
      .map( x => x*2 )
      .foreach(x => println(x))

    numbers.filter(_%2==0)
      .map(_*2)
      .foreach(println)

  }

  @Test
  def bufferRemoveOP(): Unit ={
    val buffer=ArrayBuffer("红楼梦","西游记","水浒传","三国演义");
    //移除操作
    //-
//    val newBuffer=buffer.-("西游记")
//    newBuffer.foreach(println)
    //-=
//    buffer.-=("西游记")
//    buffer.foreach(println)
    //--
    val buffer2=ArrayBuffer("红楼梦","西厢记");
//    buffer.--(buffer2).foreach(println)
    //--=
//    buffer.--=(buffer2)
//    buffer.foreach(println)

    //remove(index) remove(index,count)
//    val result=buffer.remove(2,2)
//    println("result="+result)
//    buffer.foreach(println)

    //drop(count) 返回新集合 不会修改旧集合
    //buffer.drop(2).foreach(println)
    buffer.dropRight(3).foreach(println)
    println("---")
    buffer.foreach(println)

  }

  @Test
  def defineArrayBuffer()={
    //1.定义数组缓冲对象
    //1.1通过类构建--->构造器
    val buffer1=new ArrayBuffer[Int]();
    //1.2通过对象构建
    val buffer2=ArrayBuffer.apply("Scala","Java","Python")

    val seq=20 to 30

    //2.添加元素
    //追加
    buffer1.append(1)
    buffer1.append(2,3,4)
    buffer1.appendAll(seq)
    //插入
    buffer1.insert(1,100)
    buffer1.insertAll(3,seq)
    //4.遍历数组
    //buffer1.foreach(println)
    println("---------")
    //没有=号，所以不会修改原集合，此时返回一个新集合
    buffer2.+:("Ruby").foreach(println)
    //buffer2.foreach(println)
    //出现=号，修改原集合，不会返回新集合
    buffer2.+=("Ruby")
    buffer2.foreach(println)
    println("-----")
    buffer2.+=:("Ruby").foreach(println)
    //++:
    buffer2.++:(buffer1).foreach(println)
  }
}
