package json

import java.time.LocalDateTime

import json.UserJson.userWrites
import model.{Post, User}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import json.UserJson._
import utils.DomainUtils.randomId

object PostJson {

  implicit val postWrites: Writes[Post] = {
    (post: Post) =>
      Json.obj(
        "postId" -> post.postId,
        "content" -> post.content,
        "modificationDate" -> post.modificationDate,
        "author" -> Json.toJson(post.author)
      )
  }

  implicit val postReads: Reads[Post] = (
    (__ \ "postId").readWithDefault[Long](randomId()) and
      (__ \ "content").read[String] and
      (__ \ "modificationDate").read[String].map(_ => LocalDateTime.now) and
      (__ \ "author").read[User]
    ) (Post.apply _)
}
