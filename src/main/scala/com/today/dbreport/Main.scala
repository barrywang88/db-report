package com.today.dbreport

import java.io.File
import java.util.Date

import com.today.dbreport.action.impl.{GenReportByScriptAction, GenReportBySqlAction, GenReportByTemplateBySqlAction}
import com.today.dbreport.dto.GenReportParam
import com.today.dbreport.utils.EmailUtil
import com.today.service.commons.util.DateTools
import org.kohsuke.args4j.CmdLineParser
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * 生成报表入口
  *
  * @author BarryWang create at 2018/6/1 11:02
  * @version 0.0.1
  */
object Main {
  val logger= LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val options = new ArgOptions
    val parser = new CmdLineParser(options)
    // print usage
    parser.printUsage(System.out)
    parser.parseArgument(args.toList.asJava)

    //输出文件或发送邮件必填一个
    if(options.output == null && options.email == null){
      println("请传入参数-output 或 -mailto其中之一")
      return
    }

    //生成报表地址
    var utf8Output = ""
    if (options.output != null) {
        utf8Output = new String(options.output.getBytes("UTF-8"), "UTF-8")
    } else {//本地临时文件
      val currentTime = DateTools.format(new Date(), "yyyyMMddHHmmssSSS")
      val outDir = s"${System.getProperty("user.dir")}${File.separator}output"
      var outputDir = new File(outDir)
      if(!outputDir.exists()){
        outputDir.mkdirs()
      }
      utf8Output = s"${outDir}${File.separator}${currentTime}.xlsx"
    }
    //带有Scala脚本
    if (options.script != null) {
      var templateOptional: Option[File] = None
      if (options.template != null) {
        templateOptional = Some(options.template)
      }
      val scriptOptional = Some(options.script)
      var mailtoOptional: Option[String] = None
      if (options.email != null) {
        mailtoOptional = Some(options.email)
      }
      val genReportParam = new GenReportParam(options.query, utf8Output, templateOptional, scriptOptional, mailtoOptional)
      //sql + script + jxls template
      //script + jxls tempalte
      new GenReportByScriptAction(genReportParam).execute
    } else {//无Scala脚本
      var templateOptional: Option[File] = None
      if (options.template != null){
        templateOptional = Some(options.template)
      }
      var scriptOptional : Option[String] = None
      if(options.script != null){
        scriptOptional = Some(options.script)
      }

      var mailtoOptional: Option[String] = None
      if (options.email != null){
        mailtoOptional = Some(options.email)
      }

      val genReportParam = new GenReportParam(options.query, utf8Output, templateOptional, scriptOptional, mailtoOptional)
      if (options.template != null) { //sql* + jxls template
        new GenReportByTemplateBySqlAction(genReportParam).execute
      } else { //no template + sql
        new GenReportBySqlAction(genReportParam).execute
      }
    }

    println(s"报表生成成功${utf8Output}！")
    //发送邮件
    if (options.email != null) {
      var subject = "报表工具生成报表"
      if(options.subject != null){
        subject = options.subject
      }
      EmailUtil.sendEmail(options.from, options.email.trim, subject, "生成报表请参考附件", utf8Output)
      println("邮件发送成功，请邮件附件下载相关报表！")
      //邮件发送成功， 删除本地临时文件
      if (options.output == null) {
        new File(utf8Output).deleteOnExit()
      }
    }
    logger.info(s"报表生成成功${utf8Output}")
  }
}
