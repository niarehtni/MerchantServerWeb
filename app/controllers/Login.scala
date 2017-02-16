package controllers

import javax.inject.Inject

import Auth.AuthConfigImpl
import service.AccountService
import play.api.data.Forms.text
import jp.t2v.lab.play2.auth.LoginLogout
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import views.html

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Created by renger on 2017/2/15.
  */
class Login @Inject()(protected val accountService: AccountService) extends Controller with LoginLogout with AuthConfigImpl {
  val loginForm = Form {
    mapping(
      "name" -> text,
      "password" -> text
    )(accountService.authenticate)(_.map(u => (u.name, ""))).verifying("账号或密码错误", result => result.isDefined)
  }

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.login(formWithErrors))),
      user => gotoLoginSucceeded(user.get.name)
    )
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded
  }
}
