package com.today.dbreport

import com.today.dbreport.Main.logger
import com.today.dbreport.action.impl.GenReportBySqlAction
import com.today.dbreport.dto.GenReportParam
import org.junit.Test
import org.kohsuke.args4j.CmdLineParser

/**
  * 类功能描述:
  */
class GenReportBySqlActionTest {
  @Test
  def mTest: Unit = {
    val args = Array[String]("employee", "employees:select * from employee", "employees:select * from employee", "")
    if(args.length < 3){
      logger.info("请输入正确的参数：java -jar dbreport.jar 数据库 \"SQL\" \"输出文件\"")
      logger.info("正确参数格式如下：java -jar dbreport.jar member \"select * from memeber\" \"/tmp/report/output.xls\"")
      return
    }
//    new GenReportBySqlAction(GenReportParam(args(0), args(1), args(2), None, None, None)).execute
  }
}
