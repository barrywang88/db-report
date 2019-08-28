package com.today.dbreport.utils

import java.util

import net.sf.cglib.beans.{BeanGenerator, BeanMap}

/**
  * 动态Bean生成
  *
  * @author BarryWang create at 2018/6/19 11:54
  * @version 0.0.1
  */
class DynamicBean {
  private var `object`: AnyRef = null
  private var beanMap: BeanMap = null

  def this(fieldTypeMap: util.Map[String, AnyRef]) {
    this()
    this.`object` = generateObject(fieldTypeMap)
    this.beanMap = BeanMap.create(this.`object`)
  }

  /**
    * 生成对象
    *
    * @param fieldTypeMap 域及类型Map
    * @return
    */
  private def generateObject(fieldTypeMap: util.Map[String, AnyRef]) = {
    val generator = new BeanGenerator
    val keySet = fieldTypeMap.keySet
    val i = keySet.iterator
    while (i.hasNext) {
      val key = i.next
      generator.addProperty(key, fieldTypeMap.get(key).asInstanceOf[Class[_]])
    }
    generator.create
  }

  /**
    * 设置域值
    *
    * @param filedName 域名
    * @param value     值
    */
  def setValue(filedName: String, value: AnyRef): Unit = {
    this.beanMap.put(filedName, value)
  }

  /**
    * 获取域值
    *
    * @param fieldName 域名
    * @return
    */
  def getValue(fieldName: String): AnyRef = this.beanMap.get(fieldName)

  /**
    * 最终对象
    *
    * @return
    */
  def getObject: AnyRef = this.`object`
}
