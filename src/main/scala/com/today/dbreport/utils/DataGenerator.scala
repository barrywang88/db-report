package com.today.dbreport.utils

import java.sql.{Date, ResultSet, ResultSetMetaData, Types}
import java.util.{ArrayList, HashMap => JavaHashMap, List => JavaList}

import com.today.service.commons.util.DateTools

import scala.collection.JavaConverters._
import scala.collection.mutable.{ListBuffer => ScalaList}
import scala.math.BigDecimal.RoundingMode

/**
  * 数据生成工具类
  *
  * @author BarryWang create at 2018/6/14 9:46
  * @version 0.0.1
  */
object DataGenerator {
  /**
    * 根据数据查出数据转成对象列表
    * @param database 数据库
    * @param querySql 查询SQl
    * @return
    */
  def getObjectDataList(database: String, querySql: String): ArrayList[Object] = {
    println(s"database:${database} sql:${querySql}")
    val rs = DataGenerator.getResultSet(database, querySql)
    val md = rs.getMetaData
    val colCount = md.getColumnCount
    val fieldValMap = new JavaHashMap[String, AnyRef]()
    for (i <- 0 to colCount - 1) {
      fieldValMap.put(StringUtils.underLineToCamel(md.getColumnName(i + 1)), Class.forName("java.lang.String"))
    }
    // 生成动态 Bean
    val returnList = new ArrayList[Object]();
    while (rs.next()) {
      val objectBean = new DynamicBean(fieldValMap);
      for (i <- 0 to colCount - 1) {
        val data = getData(md, i+1, rs)
        objectBean.setValue(StringUtils.underLineToCamel(md.getColumnName(i + 1)), data)
      }
//      println(">> " + objectBean.getValue("zhCnName"))
      returnList.add(objectBean.getObject)
    }
    rs.close()
    returnList
  }

  def getObjectDataList1(database: String, querySql: String): ArrayList[Object] = {
    println(s"database:${database} sql:${querySql}")
    val rs = DataGenerator.getResultSet(database, querySql)
    val md = rs.getMetaData
    val colCount = md.getColumnCount
    val fieldValMap = new JavaHashMap[String, AnyRef]()
    for (i <- 0 to colCount - 1) {
      fieldValMap.put(StringUtils.underLineToCamel(md.getColumnName(i + 1)), Class.forName("java.lang.String"))
    }
    // 生成动态 Bean
    val returnList = new ArrayList[Object]();
    while (rs.next()) {
//      val objectBean = new DynamicBean(fieldValMap)
      val nameValMap = new JavaHashMap[String, Object]()
      for (i <- 0 to colCount - 1) {
        val data = getData(md, i+1, rs)
        nameValMap.put(StringUtils.underLineToCamel(md.getColumnName(i + 1)), data)
      }
//            println(">> " + objectBean.getValue("zhCnName"))
      returnList.add(nameValMap)
    }
    rs.close()
    returnList
  }

  def underLineToCamel(name: String): String = {
    val result = new StringBuilder
    // 快速检查
    if (name == null || name.isEmpty) { // 没必要转换
      return ""
      //        } else if (!name.contains("_")) {
      //            // 不含下划线，仅将首字母小写
      //            return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
    // 用下划线将原始字符串分割
    val camels = name.split("_")
    for (camel <- camels) { // 跳过原始字符串中开头、结尾的下换线或双重下划线
      // 处理真正的驼峰片段
      if (result.length == 0) { // 第一个驼峰片段，全部字母都小写
        result.append(camel.toLowerCase)
      }
      else { // 其他的驼峰片段，首字母大写
        result.append(camel.substring(0, 1).toUpperCase)
        result.append(camel.substring(1).toLowerCase)
      }
    }
    result.toString
  }

  /**
    * 根据SQL与数据库获取表头数据及内容数据
    * @param database 数据库
    * @param querySql 查询SQl
    * @return
    */
  def getHeadsAndDataBySql(database: String, querySql: String): (ScalaList[String], ScalaList[JavaList[String]]) ={
    println(s"database:${database} sql:${querySql}")
    val rs = DataGenerator.getResultSet(database, querySql)

    val headers = new ScalaList[String]
    val md = rs.getMetaData
    val colCount = md.getColumnCount
    for (i <- 0 to colCount - 1) {
      headers+=md.getColumnLabel(i + 1)
    }

    val rows = new ScalaList[JavaList[String]]
    while (rs.next()) {
      val resultList = new ScalaList[String]
      for (i <- 0 to colCount - 1) {
        val data = getData(md, i+1, rs)
        resultList+=data
      }
      rows.+=:(resultList.asJava)
    }
    rs.close()
    (headers, rows)
  }

  /**
    * 获取数据库结果集
    * @param database 数据库
    * @param querySql 查询SQl
    * @return
    */
  def getResultSet(database: String, querySql: String): ResultSet = {
    //financeReport
    val conn = DbReportConnection.getConnection(database)
    val st = conn.createStatement()
    //select * from t_export_report_record
    st.executeQuery(querySql)
  }

  /**
    * 从ResultSet取数
    * @param md
    * @param index
    * @param rs
    * @return
    */
  def getData(md: ResultSetMetaData, index: Int, rs: ResultSet): String = {
    md.getColumnType(index)  match {
      case Types.DECIMAL => {
        val value = rs.getBigDecimal(index)
        val bigDecimal = value == null match {
          case true => "0.0000"
          case false => value.setScale(4, RoundingMode.HALF_UP).toString()
        }
        return bigDecimal
      }
      case Types.TIMESTAMP =>{
        val value = rs.getTimestamp(index)
        val timestamp = value == null match {
          case true => ""
          case false => DateTools.format(new Date(rs.getTimestamp(index).getTime))
        }
        return timestamp
      }
      case Types.DATE => {
        val value = rs.getDate(index)
        val date = value == null match {
          case true => ""
          case false => DateTools.format(rs.getDate(index), DateTools.DATE_PATTERN)
        }
        return date
      }
      case _ => {
        val value = rs.getObject(index)
        val data = value == null match {
          case true => ""
          case false => rs.getObject(index).toString
        }
        return data
      }
    }
  }

  def main(args: Array[String]): Unit = {
    DataGenerator.getObjectDataList("member", "select * from member")
  }
}
