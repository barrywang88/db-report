import ammonite.main.Router.main
import wangzx.scala_commons.sql._

import scala.beans.BeanProperty

case class Member (
	/**
		* 会员名称
		*/
	@BeanProperty
	memberName : String,
	/**
		* 积分
		*/
	@BeanProperty
	memberScore : Int,
	/**
		* 用户手机号
		*/
	@BeanProperty
	mobilePhone : String,
	/**
		* 会员类型,1:正常(支付会员);2:冻结(认证会员)
		*/
	@BeanProperty
	memberType : Int,
	/**
		* 会员生日
		*/
	@BeanProperty
	memberBirthday : java.sql.Timestamp
)

case class Dept (
								@BeanProperty
								key: String)

@main
def main(sql: String) = {
	val driverClassName="com.mysql.jdbc.Driver"
	val url="jdbc:mysql://10.10.10.36:3306/member?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true"
	val username="root"
	val password="today-36524"
	Class.forName(driverClassName)
	val conn = java.sql.DriverManager.getConnection(url, username, password)
	val rows = conn.rows[Member](sql)
//	Map("dept"-> Dept("Today"))
	Map("members"-> rows)
}