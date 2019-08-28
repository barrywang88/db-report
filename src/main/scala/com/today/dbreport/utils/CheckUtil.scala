package com.today.dbreport.utils

import java.util.regex.Pattern

import com.today.service.commons.util.DateTools

/**
  * 验证该工具类
  */
object CheckUtil {
  /**
    * 时分格式(hh:mm)正则表达式
    */
  val HOUR_MINUTE_PATTERN = Pattern.compile("[0-2]{1}[0-9]{1}:[0-6]{1}[0-9]{1}")

  /**
    * 日期字符串是否合法
    * @param s
    * @return
    */
  def isValidDate(s:String) : Boolean = {
    try{
      DateTools.parseDate(s,"yyyy-MM-dd")
      true
    }catch {
      case e: Exception => false
    }
  }

  /**
    * 指定格式日期字符串是否合法
    * @param s
    * @param pattern
    * @return
    */
  def isValidDate(s:String,pattern:String) : Boolean = {
    try{
      DateTools.parseDate(s,pattern)
      true
    }catch {
      case e: Exception => false
    }
  }

  /**
    * 是否有效的时分格式(hh:mm)
    * @param hourMibuteStr
    * @return
    */
  def isValidHourMinute(hourMibuteStr : String) : Boolean={
    HOUR_MINUTE_PATTERN.matcher(hourMibuteStr).matches()
  }

}
