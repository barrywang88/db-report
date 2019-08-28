package com.today.dbreport

import java.io.FileOutputStream

import com.today.dbreport.utils.{DbReportConnection, TemplateReader}
import org.junit.Test
import org.jxls.common.Context
import org.jxls.jdbc.JdbcHelper
import org.jxls.util.JxlsHelper
import org.slf4j.{Logger, LoggerFactory}

/**
  * TODO the class description
  *
  * @author BarryWang create at 2018/6/7 8:50
  * @version 0.0.1
  */
class SqlReport {
  val logger: Logger = LoggerFactory.getLogger(classOf[SqlReport])

  @Test
  def myTest: Unit = {
    val conn = DbReportConnection.getConnection("financeReport")
    val jdbcHelper = new JdbcHelper(conn)
    val is = TemplateReader.getTemplateInputStream("sql_demo_template.xlsx")
    try {
      val os = new FileOutputStream("C:\\Users\\lhe\\Desktop\\sql_demo_output.xls")
      try {
        val context = new Context
        context.putVar("conn", conn)
        context.putVar("jdbc", jdbcHelper)
        JxlsHelper.getInstance.processTemplate(is, os, context)
      } finally {
        if (os != null){
          os.close()
        }
      }
    } finally {
      if (is != null){
        is.close()
      }
    }
  }
}
