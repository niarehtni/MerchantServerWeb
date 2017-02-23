package auth

import auth.Role.{Admin, NormalUser}
import controllers.routes
import jp.t2v.lab.play2.auth.AuthConfig
import model.Account
import play.api.mvc.Results._
import play.api.mvc._
import service.AccountService

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect._

/**
  * Created by qugang
  */
trait AuthConfigImpl extends AuthConfig {

  /**
    * A type that is used to identify a user.
    * 'String', 'Int', 'Long' and so on
    */
  type Id = String

  /**
    * A type that represents a user in application.
    * 'User', 'Account' and so on
    */
  type User = AuthUser

  /**
    * A type that is defined by every action for authorization.
    * This sample uses the following trait.
    *
    * sealed trait Permission
    * case object Administrator extends Permission
    * case object NormalUser extends Permission
    */
  type Authority = Role

  /**
    * A 'ClassManifest' is used to retrieve an id from the Cache API.
    * Use something like this:
    */
  val idTag: ClassTag[Id] = classTag[Id]

  /**
    * The session timeout in seconds
    */
  val sessionTimeoutInSeconds: Int = 3600

  protected val accountService: AccountService

  /**
    * A function that return a 'User' object  from an 'Id'.
    * can alter the procedure to suit the real application
    */
  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] = accountService.get(id)


  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Home.index()))

  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Login.login()).withNewSession)

  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Login.login()))

  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])(implicit context: ExecutionContext): Future[Result] = {
    Future.successful(Redirect(routes.Login.login()))
  }

  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful((user.role, authority) match {
    case ("Admin", _) => true
    case ("NormalUser", NormalUser) => true
    case _ => false
  })
}