package com.gkd.workTwo

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/14 12:16
 */
class Square (centerPoint:Point, var side: Double) extends Shape (centerPoint) with Drawable{
  override def zoom(scale: Double): Unit = {
    side = side*scale;
  }
  override def draw() {
    println(s"Square center: (${pointObeject.x},${pointObeject.y}) ,E=$side")
  }
}

