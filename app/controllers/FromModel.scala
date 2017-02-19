package controllers

/**
  * Created by renger on 2017/2/19.
  */
case class FormAccount(name:String,password:String,verifyCode:String)

case class FormChangePassword(name: String,password: String,newPassword:String,verifyCode:String)

case class FormRestPassword(name:String,password: String,newPassword:String,verifyCode:String)
