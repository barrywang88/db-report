package com.today.dbreport.args4jTest

import org.kohsuke.args4j.spi.{OneArgumentOptionHandler, Setter}
import org.kohsuke.args4j.{CmdLineParser, OptionDef}

/**
  * TODO the class description
  *
  * @author BarryWang create at 2018/6/23 23:44
  * @version 0.0.1
  */
class DoubleOptionHandler(parser: CmdLineParser, option: OptionDef, setter: Setter[Double]) extends OneArgumentOptionHandler[Double](parser, option, setter) {
  override def parse(argument: String): Double = {
    argument.toDouble
  }
}
