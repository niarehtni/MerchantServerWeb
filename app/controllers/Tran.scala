package controllers


import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.util.Calendar
import javax.inject.Inject

import auth.AuthConfigImpl
import auth.Role.NormalUser
import info.folone.scala.poi.{Row, Sheet, StringCell, Workbook}
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

  val sumQueryForm = Form(
    single(
      "date" -> text
    )
  )


  def tranLs = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    Ok(html.tran(List[TranLS](), queryForm, 0, 0, "", ""))
  }

  def tranQuery() = AsyncStack(AuthorityKey -> NormalUser) { implicit request =>
    queryForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.tran(List[TranLS](), formWithErrors, 0, 0, "", ""))),
      formQuery => tranService.listCount(request.session.get("userName").getOrElse(""), formQuery.date, formQuery.orderId).flatMap {
        count =>
          tranService.listAll(request.session.get("userName").getOrElse(""), formQuery.date, formQuery.orderId, 0).map(p =>
            Ok(html.tran(p, queryForm, count, 0, formQuery.date, formQuery.orderId))
          )
      }
    )
  }

  def tranQueryCount(tranDate: String, orderId: String, index: Int, count: Int) = AsyncStack(AuthorityKey -> NormalUser) { implicit request =>
    tranService.listAll(request.session.get("userName").getOrElse(""), tranDate, orderId, index).map(p => Ok(html.tran(p, queryForm, count, index, tranDate, orderId)))
  }

  def tranSum = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    Ok(html.tranSum(None, sumQueryForm))
  }

  def tranSumPost = AsyncStack(AuthorityKey -> NormalUser) { implicit request =>
    sumQueryForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest("未知错误")),
      date => tranSumLsService.get(request.session.get("userName").getOrElse(""), date)
        .map(p => Ok(html.tranSum(p, sumQueryForm)))
    )
  }


  def export = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    Ok(html.export(List[TranLS](), queryForm))
  }

  def exportPost = AsyncStack(AuthorityKey -> NormalUser) { implicit request =>
    queryForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(html.export(List[TranLS](), formWithErrors))),
      formQuery => tranService.listAll(request.session.get("userName").getOrElse(""), formQuery.date, formQuery.orderId).map(p => {
        val oututStream = new ByteArrayOutputStream()
        val workbook = Workbook {
          Set(Sheet("日明细") {
            p.zipWithIndex.map {
              case (row, index) => {
                Row(index)(Set(StringCell(0, row.MerchantNo),
                  StringCell(1, row.TerminalNo),
                  StringCell(2, row.CardNo),
                  StringCell(3, row.TranAmt.toString),
                  StringCell(4, row.FeeAmt.toString),
                  StringCell(5, row.TranDate),
                  StringCell(6, row.TranTime),
                  StringCell(7, row.Rnn),
                  StringCell(8, row.Channel)))
              }
            }.toSet
          })
        }
        workbook.asPoi.write(oututStream)
        Ok(oututStream.toByteArray()).as("xls").withHeaders("Content-Disposition" -> "attachment; filename=tranls.xls")
      }))
  }

}
