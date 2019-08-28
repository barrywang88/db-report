package com.today.dbreport.action.impl

import java.io.{FileInputStream, FileOutputStream, InputStream, OutputStream}
import java.util.{List => JavaList}

import com.today.dbreport.action.ReportAction
import com.today.dbreport.dto.GenReportParam
import com.today.dbreport.utils.DataGenerator
import org.jxls.area.Area
import org.jxls.builder.AreaBuilder
import org.jxls.builder.xls.XlsCommentAreaBuilder
import org.jxls.common.{CellRef, Context}
import org.jxls.transform.Transformer
import org.jxls.transform.poi.PoiTransformer
import org.jxls.util.TransformerFactory
import org.slf4j.LoggerFactory

/**
  * 根据SQL及模板生成报表
  *
  * @author BarryWang create at 2018/6/14 10:03
  * @version 0.0.1
  */
class GenReportByTemplateBySqlAction(genReportParam: GenReportParam) extends ReportAction {
  val logger= LoggerFactory.getLogger(classOf[GenReportByTemplateBySqlAction])
  /**
    * 前置条件检查：动作、状态等业务逻辑
    */
  override def preCheck: Boolean = {
    genReportParam.template match {
      case Some(x) => return true
      case None => {
        logger.error("请输入模板！")
        return false
      }
    }
  }

  /**
    * 生成报表
    */
  override def generateReport: Unit = {
    val context: Context = PoiTransformer.createInitialContext
    genReportParam.querySql.toList.foreach{x=>
      val dataObj: JavaList[Object] = DataGenerator.getObjectDataList(x._1, x._3)
      context.putVar(x._2, dataObj)
    }

    val is: InputStream = new FileInputStream(genReportParam.template.getOrElse(null))
    try {
      val os: OutputStream = new FileOutputStream(genReportParam.output)
      try {
        val transformer: Transformer = TransformerFactory.createTransformer(is, os)
        val areaBuilder: AreaBuilder = new XlsCommentAreaBuilder(transformer, false)
        val xlsAreaList: JavaList[Area] = areaBuilder.build
        val xlsArea: Area = xlsAreaList.get(0)
        xlsArea.applyAt(new CellRef(s"${xlsArea.getAreaRef.getSheetName}!A1"), context)
        xlsArea.processFormulas()
        logger.info("Complete")
        transformer.write()
        logger.info("written to file")
      } finally if (os != null) os.close()
    }
    finally if (is != null) is.close()
  }
}

