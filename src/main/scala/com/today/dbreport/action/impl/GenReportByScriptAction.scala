package com.today.dbreport.action.impl

import java.io._
import java.util.{List => JavaList}

import ammonite.ops.Path
import ammonite.util.Res
import ammonite.util.Res.{Exception, Failure, Success}
import com.today.dbreport.action.ReportAction
import com.today.dbreport.dto.GenReportParam
import com.today.dbreport.utils.DataGenerator
import org.jxls.area.Area
import org.jxls.builder.xls.XlsCommentAreaBuilder
import org.jxls.common.{CellRef, Context}
import org.jxls.transform.poi.PoiTransformer
import org.jxls.util.TransformerFactory
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * 根据Scala脚本, SQL及模板生成报表
  *
  * @author BarryWang create at 2018/6/14 10:03
  * @version 0.0.1
  */
class GenReportByScriptAction(genReportParam: GenReportParam) extends ReportAction {
  val logger= LoggerFactory.getLogger(classOf[GenReportByScriptAction])
  /**
    * 前置条件检查：动作、状态等业务逻辑
    */
  override def preCheck: Boolean = {
    genReportParam.template match {
      case Some(x) => return true
      case None => {
        logger.error("请输入模板！")
        false
      }
    }
  }

  /**
    * 生成报表
    */
  override def generateReport: Unit = {
    val pathParamsArray = genReportParam.script.getOrElse("").split(";")
    //获取Scala脚本参数
    val args = pathParamsArray.zipWithIndex.filter(_._2 != 0).map{ x=>
        val paramNameValPair = x._1.split(":")
        (paramNameValPair(0), Some(paramNameValPair(1)))
    }

    //运行Scala脚本并获取数据Map
    val result: (Res[Any], Seq[(Path, Long)]) = ammonite.Main().runScript(Path(pathParamsArray(0)), args)
    val dataMap = (result._1 match {
      case Success(x) => {
        Some(x.asInstanceOf[Map[String, AnyRef]])
      }
      case Failure(msg) => logger.error(s"generate report by script failure: ${msg}"); None
      case Exception(msg, e) => logger.error(s"generate report by script exception: ${msg}", e); None
    }) match {
      case Some(x) => x
      case None => Map()
    }

    val context: Context = PoiTransformer.createInitialContext
    //设置Scala脚本获取的数据
    dataMap.foreach{x=>
      val dataObj = x._2.isInstanceOf[Seq[AnyRef]] match {
        case true => x._2.asInstanceOf[List[AnyRef]].asJavaCollection
        case _ => x._2
      }
      context.putVar(x._1, dataObj)
    }
    //设置SQL查询获取的数据
    genReportParam.querySql.toList.foreach{x=>
      val dataObj: JavaList[Object] = DataGenerator.getObjectDataList(x._1, x._3)
      context.putVar(x._2, dataObj)
    }

    val is: InputStream = new FileInputStream(genReportParam.template.getOrElse(null))
    try {
      val os: OutputStream = new FileOutputStream(genReportParam.output)
      try {
        val transformer = TransformerFactory.createTransformer(is, os)
        val areaBuilder = new XlsCommentAreaBuilder(transformer, false)
        val xlsAreaList = areaBuilder.build
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
