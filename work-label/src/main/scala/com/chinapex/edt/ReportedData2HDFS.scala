package com.chinapex.edt

import com.chinapex.label.util.HdfsUtil
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.{SaveMode, SparkSession}

import java.text.SimpleDateFormat
import java.util
import java.util.Date

/**
 * @author zhaoyijie
 * @since 2024/9/9 17:56
 */
object ReportedData2HDFS {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").appName("ReportedData2HDFS").enableHiveSupport().getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._
    // val df = spark.read.json("hdfs://192.168.1.101:9000/user/hive/warehouse/edt/ods/*.json")
    //下面的语句代替读取hdfs
    val jsonString = "{\"appId\":\"67def4af3cfb775883515c1f3244c816ffacf3f0\",\"eventCode\":\"dc_a0208\",\"eventName\":\"商品详情页-进入页面\",\"eventType\":\"2\",\"eventCategory\":null,\"eventTime\":1669086422236,\"userId\":\"1080634354\",\"userName\":null,\"attr\":{\"tenant_id\":\"1\",\"visible_window_width\":\"412\",\"sdk_type\":\"js\",\"device_model\":\"M2102K1C\",\"page_type\":\"wechat\",\"send_type\":\"code\",\"device_type\":\"phone\",\"source_page_path\":\"pages/home/home\",\"time_since_last_operation\":\"66\",\"actName\":\"\",\"goodsSPU\":\"345\",\"current_page_url\":\"subpackage/pages/shopping/product/productDetail\",\"screen_height\":\"899\",\"event_type\":\"custom\",\"project_id\":\"c2f28322-6dae-4217-9fd8-3ad3d969a491\",\"event_code\":\"dc_a0208\",\"wechat_open_id\":\"oN-hY5KOBNPEwBN1qj5TsHcSTMuk\",\"sdk_version\":\"0.2.0\",\"is_custom\":\"true\",\"current_page_name\":\"商品详情页\",\"app_id\":\"67def4af3cfb775883515c1f3244c816ffacf3f0\",\"screen_width\":\"412\",\"visible_window_height\":\"810\",\"current_page_id\":\"productDetail\",\"mallID\":\"100079\",\"userSource\":\"\",\"current_page_path\":\"subpackage/pages/shopping/product/productDetail\",\"os_version\":\"12\",\"ip\":\"222.70.218.254\",\"session_id\":\"session_1669086341772\",\"message_id\":\"3\",\"source_page_url\":\"pages/home/home\",\"is_wechat\":\"true\",\"source_page_id\":\"homePage\",\"source_page_name\":\"首页\",\"mini_program_scene\":\"1017\",\"actCode\":\"\",\"upload_time\":\"1669086422566\",\"device_brand\":\"Xiaomi\",\"os_system\":\"Android\",\"user_id\":\"1080634354\",\"current_page_query\":\"spu=345&sku=581&\",\"wechat_union_id\":\"oZyc1uGB9prstux3Af3Sm-UBXss0\",\"event_name\":\"商品详情页-进入页面\",\"apex_id\":\"oN-hY5KOBNPEwBN1qj5TsHcSTMuk\",\"network_type\":\"wifi\",\"ts\":\"1669086422236\"}}";
    // 创建 RDD
    val jsonRDD = spark.sparkContext.parallelize(Seq(jsonString))
    val df = spark.read.json(jsonRDD)
    df.printSchema()
    df.createOrReplaceTempView("data")
    val frame = spark.sql("select appId,attr.current_page_id as current_page_id, attr.current_page_name as current_page_name from data")
    frame.show()
    val dataStr = new SimpleDateFormat("yyyyMMdd").format(new Date())
    frame.write.mode(SaveMode.Overwrite).orc("data/edt/dwd_events/ds=" + dataStr)
    //frame.write.mode(SaveMode.Overwrite).parquet("hdfs://192.168.1.101:9000/user/hive/warehouse/edt/dwd_events/ds=" + dataStr)

    //val fs = FileSystem.get(new Configuration())
    //val list: util.List[String] = frame.toJSON.collectAsList()
    //HdfsUtil.write(fs, new Path("/user/hive/warehouse/edt/dwd_events/ds=" + dataStr), list, true)
    spark.sqlContext.clearCache()
    spark.stop()
    spark.close()

    /**
     * drop table if exists dwd_events;
     * CREATE EXTERNAL TABLE dwd_events
     * (`appId` string,`current_page_id` string,`current_page_name` string)
     * PARTITIONED BY (`ds` string)
     * stored as orc
     * LOCATION '/user/hive/warehouse/edt/dwd_events/';
     *
     */

    //通过msck repair修复hive分区的天数分区，由crontab每天调用一次
    /**
     * #!/bin/bash
     * # 0 15 * * * sh /home/hadoop/hhcdp-dc-app/nexus-dcp/repair_hive_partition.sh >> /home/hadoop/hhcdp-dc-app/nexus-dcp/repair_partition.log
     * source /etc/profile
     * source ~/.bash_profile
     * sql="
     * use edt;
     * msck repair table dwd_events;
     * "
     * hive -e "$sql"
     */
  }

}
