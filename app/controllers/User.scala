package controllers


import javax.inject.Inject

import auth.AuthConfigImpl
import auth.Role.{Admin, NormalUser}
import jp.t2v.lab.play2.auth.AuthElement
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.mvc.Controller
import service.AccountService
import views._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by renger on 2017/2/19.
  */
class User @Inject()(protected val accountService: AccountService) extends Controller with AuthElement with AuthConfigImpl {

  val changePasswordForm = Form {
    mapping(
      "name" -> text.verifying("用户名不允许为空", _.trim != ""),
      "oldPassword" -> text.verifying("原密码不允许为空", _.trim != ""),
      "password" -> text.verifying("密码不允许为空", _.trim != ""),
      "newPassword" -> text.verifying("密码不允许为空", _.trim != "")
    )(FormChangePassword.apply)(FormChangePassword.unapply)
  }

  val restPasswordForm = Form {
    mapping(
      "name" -> text.verifying("用户名不允许为空", _.trim != "")
    )(FormRestPassword.apply)(FormRestPassword.unapply)
  }

  def changePassword = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    Ok(html.changePassword(changePasswordForm))
  }

  def changePasswordSubmit = AsyncStack(AuthorityKey -> NormalUser) { implicit request =>
    changePasswordForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.changePassword(formWithErrors))),
      changePassword => accountService.updatePassword(changePassword.name, changePassword.newPassword).map(_ =>
        Ok(html.changePassword(changePasswordForm)).flashing("success" -> "修改密码成功!")
      )
    )
  }

  def resetPassword = StackAction(AuthorityKey -> Admin) { implicit request =>
    Ok(html.resetPassword(restPasswordForm))
  }

  def resetPasswordSubmit = AsyncStack(AuthorityKey -> NormalUser) { implicit request =>
    restPasswordForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.resetPassword(formWithErrors))),
      restPassword => accountService.updatePassword(restPassword.name, "888888").map(_ =>
        Ok(html.resetPassword(restPasswordForm)).flashing("success" -> "密码初始化成功!")
      )
    )
  }
}
