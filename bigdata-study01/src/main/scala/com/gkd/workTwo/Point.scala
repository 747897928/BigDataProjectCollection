package com.gkd.workTwo

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/14 11:22
 */
case class Point(var x:Double,var y:Double) extends Drawable{
  def shift(deltaX:Double,deltaY:Double){x+=deltaX;y+=deltaY}
}
