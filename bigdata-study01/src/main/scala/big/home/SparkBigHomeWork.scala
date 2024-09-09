package big.home

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.junit.Test

/**
 * @Description:
 * @author 水瓶座鬼才（zhaoyijie）
 * @date 2020/7/18 10:28
 */
class SparkBigHomeWork {
  val spark=SparkSession.builder()
    .master("local[*]")
    .appName("test")
    .getOrCreate()
  import spark.implicits._
  val sc=spark.sparkContext;
  Logger.getLogger("org").setLevel(Level.ERROR)
  @Test
  def preDataProcess(): Unit ={
    val value = sc.textFile("data/mv_0000030.txt")
      .filter(x => x.contains(","))
      .map(y => y.split(","))
      .map(att=>Row(att(0),att(1),att(2).trim));
    val fields = Array("CustomerID", "Rating", "Date")
      .map(fileName => StructField(fileName, StringType, nullable = true));
    val schema = StructType(fields)
    val dataFrame = spark.createDataFrame(value, schema)
      .select($"Rating", $"Date")
    dataFrame.show(false);
    dataFrame.write.option("header", "true")
      .csv("hdfs://192.168.1.101:9000/user/hadoop/bigdata/out")
  }
  @Test
  def hdfsRead(): Unit ={
    val value = sc.textFile("hdfs://192.168.1.101:9000/user/hadoop/bigdata/out/*.csv")
      .filter(x => x.contains("2004"))
      .map(y => y.split(","))
      .filter(y=>y(0).equals("5"))
      .map(y=>Array(y(0),y(1).trim().split("-")(1)))
      .map(att=>Row(att(0).toInt,att(1).toInt));
    val fields = Array("Rating", "Date")
      .map(fileName => StructField(fileName, IntegerType, nullable = true));
    val schema = StructType(fields)
    val dataFrame = spark.createDataFrame(value, schema);
    //dataFrame.show(false);
    val dataset: Dataset[(Int, Long)] = dataFrame.groupByKey(row => row.getInt(1)).count()
    dataset.show(false);
    dataset.write.option("header", "true")
      .csv("data/out")
  }

}
