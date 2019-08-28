package com.today.dbreport.action.impl

import java.io.FileOutputStream

import com.today.dbreport.Main.logger
import com.today.dbreport.action.ReportAction
import com.today.dbreport.dto.GenReportParam
import com.today.dbreport.utils.{DataGenerator, TemplateReader}
import org.jxls.builder.xml.XmlAreaBuilder
import org.jxls.common.CellRef
import org.jxls.transform.poi.PoiTransformer
import org.jxls.util.TransformerFactory

import scala.collection.JavaConverters._

/**
  * 根据SQL及默认模板生成报表
  *
  * @author BarryWang create at 2018/6/14 10:03
  * @version 0.0.1
  */
class GenReportBySqlAction(genReportParam: GenReportParam) extends ReportAction{
  /**
    * 前置条件检查：动作、状态等业务逻辑
    */
  override def preCheck: Boolean = {
    true
  }

  /**
    * 生成报表
    */
  override def generateReport: Unit = {
    val dbNameSql = genReportParam.querySql.toList.map{ x=>
      val result = DataGenerator.getHeadsAndDataBySql(x._1, x._3)
      (result._1, result._2, x._2)
    }
    val is = TemplateReader.getTemplateInputStream("dynamic_columns.xlsx")
    try {
      val os = new FileOutputStream(new String(genReportParam.output.getBytes("UTF-8"), "UTF-8"))
      try {
        val transformer = TransformerFactory.createTransformer(is, os)
        val configInputStream = TemplateReader.getTemplateInputStream("dynamic_columns.xml")
        val areaBuilder = new XmlAreaBuilder(configInputStream, transformer)
        val xlsAreaList = areaBuilder.build
        //不同SQL查询数据放入不同Sheet
        dbNameSql.zipWithIndex.foreach{x=>
            if(x._2 != 0){
              xlsAreaList.add(xlsAreaList.get(0))
            }
            val xlsArea = xlsAreaList.get(x._2)
            val context = PoiTransformer.createInitialContext
            context.putVar("headers", x._1._1.asJava)
            context.putVar("rows", x._1._2.asJava)
            // applying transformation
            xlsArea.applyAt(new CellRef(s"${x._1._3}!A1"), context)
        }
        transformer.deleteSheet("Template88886666me")
        // saving the results to file
        transformer.write()
        logger.info("Complete")
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
