package controllers

import java.io.ByteArrayOutputStream
import java.util.Properties
import javax.imageio.ImageIO
import javax.inject.Inject

import auth.AuthConfigImpl
import com.google.code.kaptcha.impl.DefaultKaptcha
import com.google.code.kaptcha.util.Config
import service.AccountService
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
      "name" -> text.verifying("用户名不允许为空", _.trim != ""),
      "password" -> text.verifying("密码不允许为空", _.trim != ""),
      "verifyCode" -> text.verifying("验证码不允许为空", _.trim != "")
    )(FormAccount.apply)(FormAccount.unapply)
  }

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }


  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.login(formWithErrors))),
      user => {
        if (request.session.get("verifyCode").get == user.verifyCode)
          accountService.get(user.name, user.password).flatMap {
            _ match {
              case None => Future.successful(BadRequest(html.login(loginForm.fill(user).withError("name", "用户名密码错误"))))
              case Some(r) => gotoLoginSucceeded(r.name)
            }
          } else Future.successful(BadRequest(html.login(loginForm.fill(user).withError("verifyCode", "验证码输入错误"))))
      }
    )
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded
  }

  def captcha = Action { implicit request =>
    val captchaPro = new DefaultKaptcha()
    captchaPro.setConfig(new Config(new Properties()))
    val text = captchaPro.createText
    val img = captchaPro.createImage(text)
    val stream = new ByteArrayOutputStream
    ImageIO.write(img, "jpg", stream)
    stream.flush()
    Ok(stream.toByteArray()).as("image/jpg").withSession(request.session + ("verifyCode" -> text))
  }
}
