name := "db-report"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.github.wangzaixiang" %% "scala-sql" % "2.0.6",
  "org.slf4j" % "slf4j-api" % "1.7.13",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.today" %% "service-commons" % "1.6-SNAPSHOT",
  "org.apache.ant" % "ant" % "1.8.2",
  "joda-time" % "joda-time" % "2.10",
  "com.typesafe" % "config" % "1.3.3",
  "org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "javax.mail" % "mail" % "1.4",
  "args4j" % "args4j" % "2.33",
  "org.jxls" % "jxls" % "2.4.5",
  "org.jxls" % "jxls-poi" % "1.0.14",
  "cglib" % "cglib-nodep" % "3.2.4",
  "com.lihaoyi" % "ammonite_2.12.6" % "1.1.1",
  "net.sf.jxls" % "jxls-core" % "1.0.6",
  "junit" % "junit" % "4.12" % Test
)