package com.today.dbreport.args4jTest

import org.junit.Test
import org.kohsuke.args4j.CmdLineParser

import scala.collection.JavaConverters._

/**
  * TODO the class description
  *
  * @author BarryWang create at 2018/6/23 20:25
  * @version 0.0.1
  */
class Args4JMyTest{

  @Test
  def test: Unit = {
    val args = Array[String]("-t", "test", "-f", "C:\\Users\\lhe\\Desktop\\dist\\cmd_list.txt", "-b", "-s", "10", "-query", "members:select * from member", "-query", "employees:select * from employee")
    try {
      val options = new ArgOptions
      val parser = new CmdLineParser(options)
      // print usage
      parser.printUsage(System.out)
      parser.parseArgument(args.toList.asJava)
      // check the options have been set correctly
      options.text.foreach(println)
//      val file = scala.io.Source.fromFile(options.file, "UTF-8")
//      file.getLines().foreach(println)
      println(options.file.getName)
      if (options.bol) println(options.bol)
      println(options.size)
      options.properties.entrySet().forEach(x=> println(s"${x.getKey}->${x.getValue}"))
//      println(options.properties.get("key1"))
//      println(options.properties.get("key2"))
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
  }
}
