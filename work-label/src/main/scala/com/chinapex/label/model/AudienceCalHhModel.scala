package com.chinapex.label.model

/**
 *
 * @param relation                  和上一组规则之间的关系 0:and  1:or
 * @param audienceCalParams         计算参数
 */
case class AudienceCalHhModel(relation : Int, audienceCalParams : List[AudienceCalParamModel])
