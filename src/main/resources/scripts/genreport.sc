import wangzx.scala_commons.sql._
import java.sql.{Date, ResultSet, ResultSetMetaData, Types}
import java.io.{FileInputStream, FileOutputStream, InputStream, OutputStream}
import java.util.{ArrayList, HashMap => JavaHashMap, List => JavaList}

import org.jxls.area.Area
import org.jxls.builder.AreaBuilder
import org.jxls.builder.xls.XlsCommentAreaBuilder
import org.jxls.common.{CellRef, Context}
import org.jxls.transform.Transformer
import org.jxls.transform.poi.PoiTransformer
import org.jxls.util.TransformerFactory

import scala.collection.JavaConverters._
import scala.collection.mutable.{ListBuffer => ScalaList}
import scala.math.BigDecimal.RoundingMode
import java.text.SimpleDateFormat
import java.util.{Calendar, Date, Locale}

import org.apache.commons.lang.time.DateFormatUtils
import java.util

import ammonite.main.Router.main
import net.sf.cglib.beans.{BeanGenerator, BeanMap}
import ammonite.ops._
import com.today.dbreport.utils.DataGenerator

class DynamicBean {
  private var `object`: AnyRef = null
  private var beanMap: BeanMap = null
  def this(fieldTypeMap: util.Map[String, AnyRef]) {
    this()
    this.`object` = generateObject(fieldTypeMap)
    this.beanMap = BeanMap.create(this.`object`)
  }
  private def generateObject(fieldTypeMap: util.Map[String, AnyRef]) = {
    val generator = new BeanGenerator
    val keySet = fieldTypeMap.keySet
    val i = keySet.iterator
    while (i.hasNext) {
      val key = i.next
      generator.addProperty(key, fieldTypeMap.get(key).asInstanceOf[Class[_]])
    }
    generator.create
  }
  def setValue(filedName: String, value: AnyRef): Unit = {
    this.beanMap.put(filedName, value)
  }
  def getValue(fieldName: String): AnyRef = this.beanMap.get(fieldName)
  def getObject: AnyRef = this.`object`
}

def underLineToCamel(name: String): String = {
	val result = new StringBuilder
	if (name == null || name.isEmpty) {
	  return ""
	}
	val camels = name.split("_")
	for (camel <- camels) {
	  if (result.length == 0) {
		result.append(camel.toLowerCase)
	  }
	  else {
		result.append(camel.substring(0, 1).toUpperCase)
		result.append(camel.substring(1).toLowerCase)
	  }
	}
	result.toString
}

def format(date: java.util.Date, patterns: String*): String = {
	if (date == null) return ""
	var pattern = "yyyy-MM-dd HH:mm:ss"
	if (patterns != null && patterns.length > 0 && patterns(0) !=null && patterns(0).length > 0) pattern = patterns(0)
	DateFormatUtils.format(date, pattern)
}

def getData(md: ResultSetMetaData, index: Int, rs: ResultSet): String = {
	md.getColumnType(index)  match {
	  case Types.DECIMAL => {
		val value = rs.getBigDecimal(index)
		val bigDecimal = value == null match {
		  case true => "0.0000"
		  case false => value.setScale(4, RoundingMode.HALF_UP).toString()
		}
		return bigDecimal
	  }
	  case Types.TIMESTAMP =>{
		val value = rs.getTimestamp(index)
		val timestamp = value == null match {
		  case true => ""
		  case false => 	format(new java.util.Date(rs.getTimestamp(index).getTime))
		}
		return timestamp
	  }
	  case Types.DATE => {
		val value = rs.getDate(index)
		val date = value == null match {
		  case true => ""
		  case false => 	format(rs.getDate(index), 	"yyyy-MM-dd")
		}
		return date
	  }
	  case _ => {
		val value = rs.getObject(index)
		val data = value == null match {
		  case true => ""
		  case false => rs.getObject(index).toString
		}
		return data
	  }
	}
}

def getResultSet(sql: String, driver: String, url: String, username: String, password: String): ResultSet = {
  Class.forName(driver)
  val conn = java.sql.DriverManager.getConnection(url, username, password)
  val st = conn.createStatement()
  st.executeQuery(sql)
}

def getObjectDataList(sql: String, driver: String, url: String, username: String, password: String): ArrayList[Object] = {
  val rs = getResultSet(sql, driver, url, username, password)
  val md = rs.getMetaData
  val colCount = md.getColumnCount
  val fieldValMap = new JavaHashMap[String, AnyRef]()
  for (i <- 0 to colCount - 1) {
    fieldValMap.put(underLineToCamel(md.getColumnName(i + 1)), Class.forName("java.lang.String"))
  }

  val returnList = new ArrayList[Object]();
  while (rs.next()) {
    val objectBean = new DynamicBean(fieldValMap);
    for (i <- 0 to colCount - 1) {
      val data = getData(md, i+1, rs)
      objectBean.setValue(underLineToCamel(md.getColumnName(i + 1)), data)
    }
    returnList.add(objectBean.getObject)
  }
  rs.close()
  returnList
}

@main
def main(sql: String, driver: String, url: String, username: String, password: String,
         tempalte: String, output: String) = {
  val objNameSqlMappingArray = sql.split(";")
  val context: Context = PoiTransformer.createInitialContext
  objNameSqlMappingArray.foreach{x =>
    val objNameSql = x.split(":")
    val dataObj: JavaList[Object] = getObjectDataList(objNameSql(1), driver, url, username, password)
    context.putVar(objNameSql(0), dataObj)
  }

	val is: InputStream = new FileInputStream(tempalte)
	try {
	  val os: OutputStream = new FileOutputStream(output)
	  try {
		val transformer: Transformer = TransformerFactory.createTransformer(is, os)
		val areaBuilder: AreaBuilder = new XlsCommentAreaBuilder(transformer, false)
		val xlsAreaList: JavaList[Area] = areaBuilder.build
		val xlsArea: Area = xlsAreaList.get(0)
		xlsArea.applyAt(new CellRef("Template!A1"), context)
		xlsArea.processFormulas()
		transformer.write()
	  } finally if (os != null) os.close()
	}finally if (is != null) is.close()
}
