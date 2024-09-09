package com.gkd.workThree

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/6/14 17:43
 */
import scala.io._
object scoreManager {
  def main(args: Array[String]): Unit = {
    val inputFile: BufferedSource =Source.fromFile("data/score.txt");
    val fileDataLine = inputFile.getLines.map{ x=>x.split("\\s+")}.toList
    val lessons: Array[String] = fileDataLine.head.drop(2)
    val stuRows = fileDataLine.tail
    val courseCounts = lessons.length
    def getInfo(lines:List [Array [String]])= {
      (for (i <- 2 to courseCounts + 1) yield {
        val eachRows = lines.map { element  => element(i).toDouble }
        (eachRows.sum, eachRows.min, eachRows.max)
      }).map(x=>{(x._1/lines.length, x._2, x._3)})
    }
    def printForm (numRows:Seq[ (Double, Double,Double)]) {
        (lessons zip numRows) foreach (x=> println(f"${x._1 + ":"}%-10s${x._2._1}%5.2f${x._2._2}%8.2f${x._2._3}%8.2f"));
    }
    val allInfo = getInfo(stuRows)
    println ("course \t average \t min \t max")
    printForm(allInfo)
    val (manLines, womanLines) = stuRows.partition(x=>x(1)=="male");
    val maleResult = getInfo(manLines)
    println ("course \t average \t min \t max")
    printForm(maleResult)
    val femaleResult = getInfo(womanLines)
    println ("course \t average \t min \t max")
    printForm(femaleResult)
  }
}

