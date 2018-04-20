package actors

object messages {

  case class NewMessage(username: String, msg: String) extends Message

  case class RegisterUser(username: String)

  case class BroadcastMsg(msg: String) extends Message

  trait Message {
    def msg: String
  }

}
