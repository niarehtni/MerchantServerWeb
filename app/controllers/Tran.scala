package controllers


import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.util.Calendar
import javax.inject.Inject

import auth.AuthConfigImpl
import auth.Role.NormalUser
import jp.t2v.lab.play2.auth.AuthElement
import model.TranLS
import play.api.mvc.Controller
import service.{AccountService, TranService, TranSumService}
import views._
import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by renger on 2017/2/24.
  */
class Tran @Inject()(protected val accountService: AccountService, protected val tranService: TranService,
                     protected val tranSumLsService: TranSumService) extends Controller with AuthElement with AuthConfigImpl {

  val queryForm = Form {
    mapping(
      "orderId" -> text,
      "date" -> text
    )(FormTranQuery.apply)(FormTranQuery.unapply)
  }


  def tranLs = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    Ok(html.tran(List[TranLS](), queryForm))
  }

  def tranQuery = AsyncStack(AuthorityKey -> NormalUser) { implicit request =>
    queryForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.tran(List[TranLS](), formWithErrors))),
      formQuery => tranService.listAll(formQuery.date, formQuery.orderId).map(p => Ok(html.tran(p, queryForm)))
    )
  }

  def tranSum = AsyncStack(AuthorityKey -> NormalUser) { implicit request =>
    tranSumLsService.get(request.session.get("userName").getOrElse(""),
      new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()))
      .map(p => Ok(html.tranSum(p)))
  }

}
