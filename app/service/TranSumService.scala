package service

import javax.inject.Inject

import model.{Account, TranSumLs}
import com.google.inject.ImplementedBy
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by renger on 2017/2/26.
  */

@ImplementedBy(classOf[TranSumServiceImp])
trait TranSumService {
  def get(merchantNo: String, tranDate: String): Future[Option[TranSumLs]]
}

class TranSumServiceImp @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with TranSumService {

  import driver.api._

  private val tranLs = TableQuery[TranLsSumTable]

  private class TranLsSumTable(tag: Tag) extends Table[TranSumLs](tag, "TRAN_LS_SUM") {
    def merchantNo = column[String]("merchantNo", O.PrimaryKey)

    def tranCount = column[Int]("tranCount")

    def tranFee = column[Double]("tranFee")

    def tranAmt = column[Double]("tranAmt")

    def tranAmt1 = column[Double]("tranAmt1")

    def tranAmt2 = column[Double]("tranAmt2")

    def tranAmt3 = column[Double]("tranAmt3")

    def tranAmt4 = column[Double]("tranAmt4")

    def tranAmt5 = column[Double]("tranAmt5")

    def tranAmt6 = column[Double]("tranAmt6")

    def tranDate = column[String]("tranDate", O.PrimaryKey)

    override def * = (merchantNo, tranCount, tranFee, tranAmt, tranAmt1, tranAmt2, tranAmt3, tranAmt4, tranAmt5, tranAmt6, tranDate) <> (TranSumLs.tupled, TranSumLs.unapply)
  }

  override def get(merchantNo: String, tranDate: String): Future[Option[TranSumLs]] = {
    db.run(tranLs.filter(p => p.merchantNo === merchantNo && p.tranDate === tranDate).result.headOption)
  }
}
