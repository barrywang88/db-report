import $ivy.`mysql:mysql-connector-java:5.1.36`
import $ivy.`com.github.wangzaixiang::scala-sql:2.0.3`

import wangzx.scala_commons.sql._
import ammonite.main.Router.{doc, main}

@main
def main(sql: String@doc("查询SQL"), driver: String@doc("JDBC驱动"),
				 url: String@doc("数据库Url"), username: String@doc("数据库用户名"),
				 password: String@doc("数据库密码")) = {
	Class.forName(driver)
	val conn = java.sql.DriverManager.getConnection(url, username, password)
	val rows = conn.rows[Row](sql);
	rows.foreach(println)
}