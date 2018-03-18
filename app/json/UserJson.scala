package json

import java.time.LocalDateTime

import model.User
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object UserJson {

  implicit val userWrites: Writes[User] = {
    (user: User) =>
      Json.obj(
        "userId" -> user.userId,
        "name" -> user.name,
        "modificationDate" -> user.modificationDate,
        "note" -> user.note
      )
  }

  implicit val userReads: Reads[User] = (
    (__ \ "userId").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "modificationDate").read[String].map(_ => LocalDateTime.now) and
      (__ \ "note").read[String]
    ) (User.apply _)
}
