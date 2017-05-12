package service

import javax.inject.Inject

import com.google.inject.ImplementedBy
import model.{TranLS}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
  * Created by renger on 2017/2/26.
  */
@ImplementedBy(classOf[TranServiceImpl])
trait TranService {
  def listAll(merchantNo:String,tranDate: String, orderId: String): Future[List[TranLS]]
  def listAll(merchantNo:String,tranDate:String,orderId:String,index:Int):Future[List[TranLS]]
  def listCount(merchantNo:String, tranDate: String, orderId: String):Future[Int]
}


class TranServiceImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with TranService {

  import driver.api._

  private val tranLs = TableQuery[TranLsTable]

  private class TranLsTable(tag: Tag) extends Table[TranLS](tag, "TRAN_LS") {
    def merchantNo = column[String]("merchantNo", O.PrimaryKey)

    def terminalNo = column[String]("terminalNo",O.PrimaryKey)

    def tranAmt = column[Double]("tranAmt",O.PrimaryKey)

    def tranDate = column[String]("tranDate",O.PrimaryKey)

    def tranTime = column[String]("tranTime",O.PrimaryKey)

    def slafAmt = column[Double]("slafAmt")

    def feeAmt = column[Double]("feeAmt")

    def rrn = column[String]("rrn",O.PrimaryKey)

    def cardNo = column[String]("cardNo")

    def channel = column[String]("channel",O.PrimaryKey)

    def fileDate = column[String]("fileDate")

    def * = (merchantNo, terminalNo, tranAmt, tranDate, tranTime, slafAmt, feeAmt, rrn,cardNo, channel,fileDate) <> (TranLS.tupled, TranLS.unapply)
  }

  override def listAll(merchantNo:String,tranDate: String, orderId: String): Future[List[TranLS]] = {
    db.run(tranLs.filter(p => (p.tranDate === tranDate || p.rrn === orderId) && p.merchantNo === merchantNo ).result.map(_.toList))
  }

  override def listAll(merchantNo:String, tranDate: String, orderId: String, index: Int): Future[List[TranLS]] = {
    db.run(tranLs.filter(p=> (p.tranDate === tranDate || p.rrn === orderId) && p.merchantNo === merchantNo).sortBy(_.merchantNo).drop(index * 10).take(10).result.map(_.toList))
  }

  override def listCount(merchantNo:String, tranDate: String, orderId: String): Future[Int] = {
    db.run(tranLs.filter(p => (p.tranDate === tranDate || p.rrn === orderId) && p.merchantNo === merchantNo).length.result)
  }
}