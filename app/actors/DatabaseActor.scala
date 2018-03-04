package actors

import javax.inject.Inject

import akka.actor.Actor
import dao.ChatDao
import model.User

class DatabaseActor @Inject()(val chatDao: ChatDao) extends Actor {

  override def receive: Receive = {
//    case
    case _ =>
  }
}

object Messages {
//  class Persist(User)
}
