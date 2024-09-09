package com.base.traitT

import java.io.FileWriter

object TraitTest {
  def main(args: Array[String]): Unit = {
    val t=new T2;
    t.info("...")
    t.debug("de...")
    t.warn("wa.....")
  }
}
//日志记录
trait Log{

  def info(message:String):Unit

  def debug(message:String):Unit={
    info("debug:"+message)
  }
  def warn(message:String):Unit={
    info("warn"+message)
  }
  def error(message:String):Unit={
    info("error"+message)
  }
}
//文件日志
trait FileLog extends Log{
  override def info(message:String)={
    //IO操作 保存到文件中
    val fw=new FileWriter("log.txt",true)
    fw.write(message)
    fw.write("\n")
    fw.flush();
    fw.close();
  }
  def file(){}
}
//控制台日志
trait ConsoleLog extends  Log {
  abstract override def info(message:String)={
    super.info(message)
    println("------")
    println(message)
  }
  def console(){}
}

class A
class B extends A

class T extends FileLog{

}
class T2 extends  FileLog with  ConsoleLog{

}
trait Logger{
  def log(msg:String) }
trait TimestampLogger extends Logger{
  abstract override def log(msg:String){
    super.log(new java.util.Date()+" "+msg )
  }
}



