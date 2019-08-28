package com.today.dbreport.dto

import java.io.File
import java.util

/**
  * 报表生成参数
  *
  * @author BarryWang create at 2018/6/15 11:53
  * @version 0.0.1
  */
case class GenReportParam(
                           /**
                             *查询SQL
                             */
                           querySql: scala.collection.mutable.ListBuffer[(String, String, String)],
                           /**
                             * 报表输出路径
                             */
                           output: String,
                           /**
                             * 模板
                             */
                           template: Option[File],
                           /**
                             * Scala脚本
                             */
                           script: Option[String],
                           /**
                             * 发送邮箱
                             */
                           mailto: Option[String])
