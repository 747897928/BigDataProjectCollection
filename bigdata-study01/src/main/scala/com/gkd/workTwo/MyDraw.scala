package com.gkd.workTwo

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/14 11:24
 */
object MyDraw{
  def main(args: Array[String]) {
    val p=Point(10,10)
    p.draw();
    val line1 = new Line(Point(0,0),Point(10,10))
    line1.draw();
    line1.moveTo(Point(15,15)) //移动到一个新的点
    line1.draw();
    line1.zoom(0.5) //放大两倍
    line1.draw();
    val squr= new Square(Point(10,10),5)
    squr.draw();
    squr.moveTo(Point(30,20))
    squr.draw();
    squr.zoom(0.5)
    squr.draw();
  }
}
