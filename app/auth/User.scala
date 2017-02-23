package auth

/**
  * Created by renger on 2017/2/22.
  */
trait AuthUser {
  val name:String
  val password:String
  val role:String
}
