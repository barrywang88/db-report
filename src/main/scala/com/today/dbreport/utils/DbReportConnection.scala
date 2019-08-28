package com.today.dbreport.utils

import java.sql.{Connection, DriverManager}

import com.typesafe.config.ConfigFactory

/**
  * 获取数据库连接
  *
  * @author BarryWang create at 2018/6/7 8:23
  * @version 0.0.1
  */
object DbReportConnection {
  /**
    * 获取数据库连接
    */
  def getConnection(database: String): Connection ={
    val databaseConfig = ConfigReader.getConfig()
    Class.forName(databaseConfig.getString(s"${database}.driverClassName"))
    DriverManager.getConnection(databaseConfig.getString(s"${database}.url"),
                                databaseConfig.getString(s"${database}.username"),
                                databaseConfig.getString(s"${database}.password"))
  }
}
