package com.chinapex.label.model

/**
 *
 * @param tableName    执行sql对应的数据表
 * @param sql          执行sql
 * @param relation     和上一组规则之间的关系 0:and  1:or
 */
case class AudienceCalParamModel(tableName : String, sql : String, relation : Int)
