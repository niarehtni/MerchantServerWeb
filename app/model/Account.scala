package model

import auth.AuthUser

/**
  * Created by renger on 2017/2/16.
  */
case class Account(name:String,password:String,role:String) extends AuthUser
