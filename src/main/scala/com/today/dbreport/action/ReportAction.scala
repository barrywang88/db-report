package com.today.dbreport.action

import org.slf4j.{Logger, LoggerFactory}

import scala.util.control.NonFatal

/**
  * 报表Action
  *
  * @author BarryWang create at 2018/6/14 10:03
  * @version 0.0.1
  */
trait ReportAction {
  /**
    *报表生成流程
    */
  def execute: Unit = {
    try {
      //前置检查失败,不再执行action
      if(!preCheck){
        return
      }
      //生成报表
      generateReport
    } catch {
      case NonFatal(e) =>
        val logger: Logger = LoggerFactory.getLogger(classOf[ReportAction])
        logger.error(e.getMessage, e)
        throw e
    } finally {
    }
  }

  /**
    * 前置条件检查：动作、状态等业务逻辑
    */
  def preCheck: Boolean

  /**
    * 生成报表
    */
  def generateReport
}
