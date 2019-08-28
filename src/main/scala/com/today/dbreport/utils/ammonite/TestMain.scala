package com.today.dbreport.utils.ammonite

import com.today.dbreport.utils.ammonite.TestUtils._

object TestMain{
  def main(args: Array[String]): Unit = {
   val evaled = exec("basic\\Hello.sc")
 /*   val hello = "Hello"
    // Break into debug REPL with
    ammonite.Main(
      predefCode = "println(\"Starting Debugging!\")"
    ).run(
      "hello" -> hello,
      "fooValue" -> foo()
    )*/
  }
  def foo() = {println("23242")}
}
