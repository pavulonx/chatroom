package controllers

import javax.inject.{Inject, Singleton}

import dao.ChatDao
import model.Post
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import json.PostJson._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChatController @Inject()(cc: ControllerComponents,
                               dao: ChatDao,
                               implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def index: Action[AnyContent] = Action.async(
    dao.findAllUsers().map(o => Ok(views.html.chatroom("Chatroom", o)))
  )

  def tell: Action[JsValue] = Action.async(parse.json) {
    request: Request[JsValue] => {
      val post: Post = Json.fromJson[Post](request.body).get
      tellMsg(post).map(p => Ok(Json.toJson(p)))
    }
  }

  private def tellMsg(post: Post): Future[Post] = {
    dao.save(post)
  }

  def recent(count: Int): Action[AnyContent] = Action.async {
    dao.findPosts(count).map(rp => Ok(Json.toJson(rp)))
  }
}

object ChatController {
  val USERNAME = "name"

}