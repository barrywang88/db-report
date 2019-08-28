package com.today.dbreport

import java.io.File

import org.kohsuke.args4j.Option
import org.slf4j.LoggerFactory


/**
  * 数据库报表生成命令行参数定义
  *
  * @author BarryWang create at 2018/6/23 20:21
  * @version 0.0.1
  */
class ArgOptions {
  val logger= LoggerFactory.getLogger(classOf[ArgOptions])

  /**
    * 对象名及查询SQL脚本对，中间用英文分号":"隔开
    */
  val query = new scala.collection.mutable.ListBuffer[(String, String, String)];
  @Option(name = "-q",
          aliases = Array("-query"),
          metaVar = "<db>:<objectName>:<sql>",
          usage = "对象名及查询SQL脚本对，中间用英文分号“:”隔开, 例如database:objectName:sql。（String）")
  def setProperty(property: String): Unit = {
    var arr = property.split(":")
    arr.length match {
      case 3 => query.+=((arr(0), arr(1), arr(2)))
      case _ => logger.info("-query 传入参数格式错误, 正确格式: <db>:<objectName>:<sql>")
    }
  }

  /**
    * JXLS Excel模板文件绝对路径
    */
  @Option(name = "-t",
          aliases = Array("-template"),
          metaVar = "<template file>",
          usage = "JXLS Excel模板文件绝对路径， 请参考：http://jxls.sourceforge.net/reference/simple_exporter.html。(File)" )
  var template: File = null

  /**
    * Scala脚本文件
    */
  @Option(name = "-s",
          aliases = Array("-script"),
          metaVar = "<scala script file path>",
          usage = "Scala脚本文件， 请参考：http://ammonite.io/#ScalaScripts。(String)")
  var script: String = null

  /**
    * 输出Excel文件
    */
  @Option(name = "-o",
          aliases = Array("-output"),
    /* required = true,handler = classOf[StringArrayOptionHandler],*/
          metaVar = "<output excel file>",
          usage = "输出Excel文件绝对路径。(File)")
  var output: String = null

  /**
    * 输出Excel文件
    */
  @Option(name = "-m",
    aliases = Array("-mailto"),
    metaVar = "<email>",
    usage = "生成报表发送邮箱，多个使用英文分号“;”分割。(String)")
  var email: String = null

  /**
    * 邮件主题
    */
  @Option(name = "-sub",
    aliases = Array("-subject"),
    metaVar = "<subject>",
    usage = "邮件主题。(String)")
  var subject: String = null

  /**
    * 邮件发送者
    */
  @Option(name = "-f",
    aliases = Array("-from"),
    metaVar = "<from>",
    usage = "邮件发送者。(String)")
  var from: String = null
}
