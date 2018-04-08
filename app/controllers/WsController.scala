package controllers

import actors.WsActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import javax.inject._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import services.WsConnectionRepository

import scala.concurrent.ExecutionContext

@Singleton
class WsController @Inject()(cc: ControllerComponents,
                             wsConnectionRepository: WsConnectionRepository)
                            (implicit actorSystem: ActorSystem,
                             exec: ExecutionContext,
                             mat: Materializer) extends AbstractController(cc) {

  def socket: WebSocket = WebSocket.accept[String, String](_ => {
    ActorFlow.actorRef(output => WsActor.props(output, wsConnectionRepository))
  }
  )

}
