package controllers

import javax.inject.{Inject, Singleton}

import dao.ChatDao
import model.{Post, User}
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, Action, ControllerComponents, Request}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatController @Inject()(cc: ControllerComponents,
                               dao: ChatDao,
                               implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action.async(
    dao.findAllUsers().map(o => Ok(views.html.chatroom("Chatroom", o)))
  )

  def tell: Action[JsValue] = Action.async(parse.json) {
    request: Request[JsValue] => {
      val username = request.headers.get(ChatController.USERNAME).get
      val msg = (request.body \ "message").as[String]
      tellMsg(username, msg).map(m => Ok(m.toString))
    }
  }

  private def tellMsg(username: String, msg: String): Future[Post] = {
    val user = User(1L, username)
    dao.save(user).flatMap(
      _ => dao.save(Post(1l, msg, author = user))
    )
  }
}

object ChatController {
  val USERNAME = "username"
}