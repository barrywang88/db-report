package com.today.dbreport.args4jTest

import java.io.File
import java.util

import org.kohsuke.args4j.Option


/**
  * TODO the class description
  *
  * @author BarryWang create at 2018/6/23 20:21
  * @version 0.0.1
  */
class ArgOptions {
  @Option(name = "-t", required = true,/* handler = classOf[StringArrayOptionHandler],*/ aliases = Array("-text"), usage = "use given information(String)")
  var text:Array[String] = null
  @Option(name = "-b", usage = "display current time(boolean)")
  var bol = false
  @Option(name = "-s", aliases = Array("-size"), usage = "use given size(Integer)")
  var size = 0
  @Option(name = "-f", aliases = Array("-file"), metaVar = "<file>", usage = "use given file(File)")
  var file:File = null

  val properties = new util.HashMap[String, String]();
  @Option(name = "-query", metaVar = "<property>:<value>", usage = "use value for given property(property:value)")
  def setProperty(property: String): Unit = {
    var arr = property.split(":")
    properties.put(arr(0), arr(1))
  }
}
