package controllers

/**
  * Created by renger on 2017/2/19.
  */
case class FormAccount(name:String,password:String,verifyCode:String)

case class FormChangePassword(name: String,oldPassword:String,password: String,newPassword:String)

case class FormRestPassword(name: String)

case class FormTranQuery(text:String)
