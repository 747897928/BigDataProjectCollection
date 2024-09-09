package com.gkd.workTwo

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/14 12:13
 */
abstract class Shape (var pointObeject:Point) {
  def moveTo (newPointObeject:Point) {
    pointObeject = newPointObeject;
  }
  def zoom(scale: Double);
}
