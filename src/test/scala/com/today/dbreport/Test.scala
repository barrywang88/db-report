package com.today.dbreport

import ammonite.ops.Path
import ammonite.util.Res
import ammonite.util.Res.{Exception, Failure, Success}

object Test {
  def main(args: Array[String]): Unit = {
    val args = Seq(("x", Some("2")), ("y", Some("3")))
    //运行Scala脚本并获取数据Map
    val result: (Res[Any], Seq[(Path, Long)]) = ammonite.Main().runScript(Path("/home/barry/data/projects/db_report/src/main/resources/scripts/Test.sc"), args)
    val dataMap = (result._1 match {
      case Success(x) => {
        Some(x.asInstanceOf[Map[String, AnyRef]])
      }
      case Failure(msg) => println(s"generate report by script failure: ${msg}"); None
      case Exception(msg, e) => println(s"generate report by script exception: ${msg}", e); None
    }) match {
      case Some(x) => x
      case None => Map()
    }

    println(dataMap)
  }
}
