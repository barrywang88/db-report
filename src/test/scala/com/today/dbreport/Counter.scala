package com.today.dbreport

import java.util.concurrent.atomic.AtomicInteger

trait Counter {
  val counter = new AtomicInteger
  def execute: Unit = {
    if(counter.intValue() > 10){
      println(s"1111111${counter.intValue()}")
      return
    }
    counter.incrementAndGet()
    println(s"2222222${counter.intValue()}")
    counter.decrementAndGet()
    println(s"3333333${counter.intValue()}")
  }
}
