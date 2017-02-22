package controllers

import javax.inject.Inject

import auth.AuthConfigImpl
import auth.Role.NormalUser
import jp.t2v.lab.play2.auth.AuthElement
import model.Account
import play.api.mvc.Controller
import service.AccountService
import views._

class Home @Inject()(protected val accountService: AccountService) extends Controller with AuthElement with AuthConfigImpl {


  /**
    * Display home area only if user is logged in.
    */
  def index = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    Ok(
      html.home(Account("name", "password", "administrator"))
    )
  }

}