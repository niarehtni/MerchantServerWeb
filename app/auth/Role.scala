package auth

/**
  * Created by renger on 2017/2/22.
  */

sealed trait Role

object Role {

  case object Admin extends Role

  case object NormalUser extends Role

  def valueOf(value: String): Role = value match {
    case "Admin" => Admin
    case "NormalUser" => NormalUser
    case _ => throw new IllegalArgumentException()
  }

}
