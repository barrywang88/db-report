# db-report
灵活根据数据库表数据生成各种形式报表

 ## 1.工具说明
 ### 1.1 部署目录结构说明
     /tmp/finance/db-report
     |   db_report.jar                           --报表工具可运行Jar
     +---config
     |       config.conf                         --数据库配置文件
     +---logs
     |   \---db-report
     |           detail-db-report.2018-06-29.log --工具日志
     +---output
     |       20180629104146invoice-store.xlsx    --报表输出目录
     \---report
         +---runscript                           --shell启动工具脚本
         |   +---inventory-stock
         |   |       genInventoryStockReport.sh
         |   +---invoice-logistics
         |   |       genInvoiceReport.sh
         \---template                           --报表Excel模板
                 check_account_template.xls
                 invoice_line_template.xls
         \---scala-script                       --Scala脚本
                 getMemberInfo.sc
                 
 ### 1.2 启动参数及说明
     -q (-query) <db>:<objectName>:<sql>             : 数据库:对象名:查询SQL脚本，中间用英文分号“:”隔开, 请避免SQL中使用英文":"。
                                                       其中若传入模板时，对象名代表模板内EL表达式的key值。若不传入模板，对象名就是生成Excel当前sheet的名称。
                                                       可以传入多个-q (-query) <db>:<objectName>:<sql>组合， 多个会分别生成报表的多个sheet中。（String）
     -s (-script) <scalaScript;paramName:paramValue> : Scala脚本文件， 请返回一个Scala Map[String, AnyRef]。如果scala脚本需要传入参数, 请脚本后加分号";", 再加参数名:参数值。
                                                       Scala脚本请参考：http://ammonite.io/#ScalaScripts。(String)
     -t (-template) <template file>                  : JXLS Excel模板文件绝对路径， 请参考：http://jxls.sourceforge.net/reference/simple_exporter.html。(File)
     -o (-output) <output excel file>                : 输出Excel文件绝对路径。(File)
     -m (-mailto) <email>                            : 生成报表发送邮箱，多个使用英文分号“;”分割。(String)
     -sub (-subject) <email subject>                 : 邮件主题。(String)
     -f(-from) <email from>                          : 邮件发送者。(String)
 
 ### 1.3 命令举例
     1.3.1 根据SQL生成报表
            java -jar db_report.jar -q member:会员信息:"select * from member" -o "/tmp/db-report/output/member.xlsx" -mailto "XXX@qq.com;YYY@qq.com" -sub "会员信息表"
            java -jar db_report.jar -q member:会员信息:"select * from member" -q order_db:订单信息:"select * from orders" -o "/tmp/db-report/output/memberOrders.xlsx" -mailto "XXX@qq.com"
     1.3.2 根据SQL+模板生成报表
            java -jar db_report.jar -q member:members:"select * from member" -t "/tmp/db-report/report/template/member-template.xls" -o "/tmp/db-report/output/member.xlsx" -mailto "XXX@qq.com"
     1.3.3 根据Scala脚本+模板生成报表
            java -jar db_report.jar -q -s "/tmp/db-report/report/scala-script/getMemberInfo.sc" -t "/tmp/db-report/report/template/member-template.xls" -o "/tmp/db-report/output/member.xlsx" -"mailto XXX@qq.com"
     1.3.4 根据SQL+Scala脚本+模板生成报表
            java -jar db_report.jar -q -q member:members:"select * from member" -s "/tmp/db-report/report/scala-script/getMemberInfo.sc" -t "/tmp/db-report/report/template/member-template.xls" -o "/tmp/db-report/output/member.xlsx" -mailto "XXX@qq.com"
