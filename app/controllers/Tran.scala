package controllers


import javax.inject.Inject

import auth.AuthConfigImpl
import auth.Role.NormalUser
import jp.t2v.lab.play2.auth.AuthElement
import model.Account
import play.api.data.Form
import play.api.mvc.Controller
import service.AccountService
import views._


/**
  * Created by renger on 2017/2/24.
  */
class Tran @Inject()(protected val accountService: AccountService) extends Controller with AuthElement with AuthConfigImpl {

//  val tranQueryMoblie = Form{
//    mapping(
//      "name" ->
//    )
//  }

//  def tranLs = StackAction(AuthorityKey -> NormalUser) { implicit request =>
//    Ok(html.tran)
//  }

//  def tranQueryMoblie = AsyncStack(AuthorityKey -> NormalUser){ implicit request=>
//    Ok(html.tran)
//  }
}
