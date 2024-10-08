package com.briup.stream

import java.io.PrintWriter
import java.net.ServerSocket

import scala.io.Source

/**
 * 数据模拟发生器
 **/
object ShopSimulation {

  // 定义随机获取整数的方法
  def index(length: Int) = {
    import java.util.Random
    val rdm = new Random
    rdm.nextInt(length)
  }

  def main(args: Array[String]) {
    // 调用该模拟器需要三个参数，分为为文件路径、端口号和间隔时间(单位:毫秒)
    //    if (args.length != 3) {
    //      System.err.println("Usage: <filename> <port> <millisecond>")
    //      System.exit(1)
    //    }
    println("模拟数据器启动！！！")
    // 获取指定文件总的行数
    val filename = "data/students.csv";
    //args(0)
    val lines = Source.fromFile(filename).getLines.toList
    val filerow = lines.length

    // 指定监听某端口，当外部程序请求时建立连接
    val serversocket = new ServerSocket(8888);

    while (true) {
      //监听8090端口，获取socket对象
      val socket = serversocket.accept()
      //      println(socket)
      new Thread() {
        override def run = {
          println("Got client connected from: " + socket.getInetAddress)
          val out = new PrintWriter(socket.getOutputStream(), true)
          while (true) {
            Thread.sleep(10000)
            // 当该端口接受请求时，随机获取某行数据发送给对方
            val content = lines(index(filerow)) + "," + (scala.util.Random.nextInt(10) + scala.util.Random.nextDouble())
            println(content)
            out.write(content + '\n')
            out.flush()
          }
          socket.close()
        }
      }.start()
    }
  }
}
