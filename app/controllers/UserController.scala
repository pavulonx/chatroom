package controllers

import javax.inject.{Inject, Singleton}

import dao.ChatDao
import json.UserJson._
import model.User
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, ControllerComponents, Request}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               dao: ChatDao,
                               implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def register: Action[JsValue] = Action.async(parse.json) {
    request: Request[JsValue] => {
      val username = (request.body \ "username").as[String]
      login(username).map(m => Ok(Json.toJson(m)))
    }
  }

  def update: Action[JsValue] = Action.async(parse.json) {
    request: Request[JsValue] => {
      val user: User = Json.fromJson[User](request.body).get
      update(user).map(m => Ok(Json.toJson(m)))
    }
  }

  def delete: Action[JsValue] = Action.async(parse.json) {
    request: Request[JsValue] => {
      val user: User = Json.fromJson[User](request.body).get
      delete(user.userId).map {
        case Some(u) => Ok(Json.toJson(u))
        case None => NotFound("Cannot find user")
      }
    }
  }

  private def login(username: String): Future[User] = {
    dao.findUser(username).flatMap {
      case None =>
        val user = User(name = username)
        dao.save(user)
      case Some(u) => Future(u)
    }
  }

  def update(user: User): Future[User] = {
    dao.updateUser(user)
  }

  def delete(userId: Long): Future[Option[User]] = {
    dao.deleteUser(userId)
  }
}
