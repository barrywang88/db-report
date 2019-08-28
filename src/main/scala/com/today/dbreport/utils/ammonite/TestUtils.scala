package com.today.dbreport.utils.ammonite

import ammonite.ops.ImplicitWd._
import ammonite.ops._

/**
  * Created by haoyi on 6/4/16.
  */
object TestUtils {
  val scalaVersion = scala.util.Properties.versionNumberString
  val javaVersion = scala.util.Properties.javaVersion
  val ammVersion = ammonite.Constants.version
  val executable = /*Path(sys.env("AMMONITE_ASSEMBLY"))*/"D:\\MyAPPs\\amm"
  val intTestResources = pwd/'src/'main/'resources
  val replStandaloneResources = intTestResources/'ammonite/'integration
  val shellAmmoniteResources = pwd/'shell/'src/'main/'resources/'ammonite/'shell
  val emptyPrefdef = shellAmmoniteResources/"empty-predef.sc"
  val exampleBarePredef = shellAmmoniteResources/"example-predef-bare.sc"

  //we use an empty predef file here to isolate the tests from external forces.
  def execBase(script: RelPath, extraAmmArgs: Seq[String], args: Seq[String]) = {
    val scriptFile = /*replStandaloneResources+"\\"+*/script
    %%bash(
      executable,
      extraAmmArgs,
      "--no-remote-logging",
      "--home",
      tmp.dir(),
      scriptFile,
      args
    )
  }
  def exec(script: RelPath, args: String*) = execBase(script, Nil, args)
  def execSilent(script: RelPath, args: String*) = execBase(script, Seq("-s"), args)

  /**
    *Counts number of non-overlapping occurrences of `subs` in `s`
    */
  def substrCount(s: String, subs: String, count: Int = 0, ptr: Int = 0): Int = {
    s.indexOf(subs, ptr) match{
      case -1 => count
      case x => substrCount(s, subs, count+1, x + subs.length)
    }
  }
}
