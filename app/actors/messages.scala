package actors

object messages {

  case class NewMessage(username: String, message: String)

  case class RegisterUser(username: String)

}
