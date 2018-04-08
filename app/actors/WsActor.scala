package actors

import actors.messages.BroadcastMsg
import akka.actor.{Actor, ActorRef, Props}
import services.WsConnectionRepository

class WsActor(outputActor: ActorRef, wsConnRepository: WsConnectionRepository) extends Actor {

  override def receive: Receive = {

    case BroadcastMsg(msg) => outputActor ! msg

    case x: String => println(x)
      outputActor ! "{\"status\":\"OK\"}"
      wsConnRepository.foreach(_ ! BroadcastMsg(x))

    case _ => println("Received unhandled msg")
  }

  override def preStart(): Unit = {
    wsConnRepository.in(self)
    super.preStart()
  }

  override def postStop(): Unit = {
    wsConnRepository.out(self)
    super.postStop()
  }

}

object WsActor {
  def props(outputActor: ActorRef, wsConnectionRepository: WsConnectionRepository): Props =
    Props(new WsActor(outputActor, wsConnectionRepository))
}

