package com.today.dbreport.dto

/**
  * @author dapeng-tool
  */
case class Member (
                    /**
                      * 会员名称
                      */
                    memberName : String,

                    /**
                      * 积分
                      */
                    memberScore : Int,

                    /**
                      * 用户手机号
                      */
                    mobilePhone : String,

                    /**
                      * 会员类型,1:正常(支付会员);2:冻结(认证会员)
                      */
                    memberType : Int,

                    /**
                      * 会员生日
                      */
                    memberBirthday : java.sql.Timestamp
                  )
