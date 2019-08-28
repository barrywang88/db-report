package com.today.dbreport.utils

import org.slf4j.LoggerFactory

/**
  * 类功能描述：Debug日志追踪
  *
  * @author barry create at 18-8-29 下午3:41
  * @version 1.0.0
  */
object Debug {
  val LOGGER = LoggerFactory.getLogger(getClass)
  val counter = collection.mutable.Map[String, Int]() // label -> count
  val times = collection.mutable.Map[String, Long]() // label - time(ns)

  /**
    *追踪代码块
    * @param label 标签名
    * @param codeBlock 代码块
    * @tparam T 返回结果类型
    * @return
    */
  def trace[T](label: String)(codeBlock: => T) = {
    val t0 = System.nanoTime()
    val result = codeBlock
    val t1 = System.nanoTime()
    counter.get(label).map(_counter => counter.put(label, _counter + 1)).orElse(counter.put(label, 1))
    times.get(label).map(cost => times.put(label, cost + (t1 - t0))).orElse(times.put(label, t1 - t0))
    result
  }

  /**
    * 打印日志
    */
  def info(): Unit = {
    LOGGER.warn("FinallyDone...")
    LOGGER.warn(s"counter:${counter}")
    LOGGER.warn(s"times:${times.map { case (label, cost) => (label, cost / 1000000)}}ms")
  }

  /**
    * 重新计数
    */
  def reset(): Unit = {
    counter.clear()
    times.clear()
  }
}
