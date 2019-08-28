package com.today.dbreport

import ammonite.ops.Path
import ammonite.util.Res
import com.today.dbreport.utils.{ConfigReader, DbReportConnection}
import com.typesafe.config.ConfigFactory
import org.junit.Test

/**
  * 脚本生成报表
  *
  * @author BarryWang create at 2018/6/19 15:36
  * @version 0.0.1
  */
class GenReport {
  @Test
  def mytest: Unit = {
    val database = "member"
    val tempalte = classOf[GetDataFromDBTest].getClass.getClassLoader.getResource("templateForTest/my_template.xls").getPath.substring(1)
    val output = "C:\\Users\\lhe\\Desktop\\output.xlsx"

    val databaseConfig = ConfigReader.getConfig()
    val driver = databaseConfig.getString(s"${database}.driverClassName")
    val url = databaseConfig.getString(s"${database}.url")
    val username = databaseConfig.getString(s"${database}.username")
    val password = databaseConfig.getString(s"${database}.password")
    val sql = "members:select * from member;memberCoupons:select * from member_coupon"

    val args = Seq( ("sql", Some(sql)),
                    ("driver", Some(driver)),
                    ("url", Some(url)),
                    ("username", Some(username)),
                    ("password", Some(password)),
                    ("tempalte", Some(tempalte)),
                    ("output", Some(output)))
    val script = classOf[GetDataFromDBTest].getClass.getClassLoader.getResource("scripts/genreport.sc").getPath.substring(1)
    val result: (Res[Any], Seq[(Path, Long)]) = ammonite.Main().runScript(Path(script), args)
    println("----------" + result._1)
  }
}
