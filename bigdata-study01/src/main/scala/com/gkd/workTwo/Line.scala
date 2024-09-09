package com.gkd.workTwo

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/14 12:15
 */
class Line (startPoint:Point, var endPoint:Point) extends Shape (startPoint) with Drawable {
  override def draw() {
    println(s"Line: (${pointObeject.x}, ${pointObeject.y})--(${endPoint.x},${endPoint.y})")
  }

  override def moveTo (newPointObeject:Point) {
    val newX=newPointObeject.x - pointObeject. x;
    val newY=newPointObeject.y - pointObeject.y;
    endPoint.shift(newX, newY)/*移动起点*/
    pointObeject = newPointObeject ;/*移动终点*/
  }
  override def zoom (scale : Double) {
    val middlePointX=(endPoint.x + pointObeject.x) / 2;
    val middlePointY=(endPoint.y + pointObeject.y) / 2;
    val middlePoint = Point(middlePointX,middlePointY);
    pointObeject.x = middlePoint.x + scale * (pointObeject.x - middlePoint.x)
    pointObeject.y = middlePoint.y + scale * (pointObeject.y - middlePoint.y)
    endPoint.x = middlePoint.x + scale * (endPoint.x - middlePoint.x)
    endPoint.y = middlePoint.y + scale * (endPoint.y - middlePoint.y)
  }
}
