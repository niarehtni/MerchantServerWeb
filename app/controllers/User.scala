package controllers


import javax.inject.Inject

import auth.AuthConfigImpl
import auth.Role.NormalUser
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
      "password" -> text.verifying("密码不允许为空", _.trim != ""),
      "newPassword" -> text.verifying("密码不允许为空", _.trim != ""),
      "verifyCode" -> text.verifying("验证码不允许为空", _.trim != "")
    )(FormChangePassword.apply)(FormChangePassword.unapply)
  }

  def changePassword = StackAction(AuthorityKey -> NormalUser){ implicit request=>
    Ok(html.changePassword(changePasswordForm))
  }

  def changePasswordSubmit = AsyncStack(AuthorityKey -> NormalUser){ implicit request=>
    changePasswordForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.changePassword(formWithErrors))),
      changePassword => accountService.updatePassword(changePassword.name,changePassword.newPassword).map(_=>
        Ok(html.changePassword(changePasswordForm))
      )
    )
  }
}
