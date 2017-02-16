package service

import javax.inject.Inject

import com.google.inject.ImplementedBy
import model.Account
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@ImplementedBy(classOf[AccountServiceImpl])
trait AccountService {
  def listAll(): Future[List[Account]]

  def add(user: Account): Future[String]

  def delete(name: String): Future[Int]

  def get(name: String): Future[Option[Account]]

  def authenticate(name: String, password: String): Option[Account]
}

class AccountServiceImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] with AccountService {

  import driver.api._

  private val users = TableQuery[UsersTable]

  private class UsersTable(tag: Tag) extends Table[Account](tag, "Account") {
    def name = column[String]("name", O.PrimaryKey)

    def password = column[String]("password")

    def role = column[String]("role")

    def * = (name, password, role) <> (Account.tupled, Account.unapply)
  }

  def listAll(): Future[List[Account]] = db.run(users.result).map(_.toList)

  def add(user: Account): Future[String] = db.run(users += user).map(_ => "User successfully added").recover {
    case ex: Exception => ex.getCause.getMessage
  }

  def delete(name: String): Future[Int] = {
    db.run(users.filter(_.name === name).delete)
  }

  def get(name: String): Future[Option[Account]] = db.run(users.filter(_.name === name).result.headOption)

  def authenticate(name: String, password: String): Option[Account] = {
    Await.result(db.run(users.filter(p => p.name === name && p.password === password).result.headOption), Duration.Inf)
  }
}
