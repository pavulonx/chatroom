package dao

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId}
import javax.inject.{Inject, Singleton}
import concurrent.duration._

import database.DB
import model.{Post, User}

import scala.collection.SortedSet

//@Singleton
class RDChatDao @Inject()(db: DB) extends ChatDao {

  import generated.Tables._
  import org.jooq.impl.DSL

  implicit def toTimestamp(dateTime: LocalDateTime): Timestamp = Timestamp.valueOf(dateTime)

  implicit def fromTimestamp(timestamp: Timestamp): LocalDateTime = timestamp.toLocalDateTime

  override def save(user: User): User = {
    db.q(
      _.insertInto(USERS, USERS.NAME, USERS.NOTE, USERS.CREATION_DATE)
        .values(user.name, user.note, user.creationDate)
        .execute()
    )
    user
  }

  override def findUser(username: String): Option[User] = {
    val eventualUsers =
      db.q(
        _.selectFrom(USERS).where(USERS.NAME.eq(username))
          .fetchOneInto(classOf[User])
      )
    eventualUsers.result(1.second)
    eventualUsers.value.map(_.getOrElse(null))
    //TODO: shitty code - refactor to futures
  }

  override def updateUser(user: User): User = {
    db.q(
      _.update(USERS)
        .set(USERS.NAME, user.name)
        .set(USERS.NOTE, user.note)
        .where(USERS.USER_ID eq user.userId)
        .execute()
    )
    user
  }

  override def deleteUser(username: String): User = {
    val maybeUser = findUser(username)
    db.q(
      _.deleteFrom(USERS)
        .where(USERS.NAME eq username)
        .execute()
    )
    maybeUser.get
  }

  override def save(post: Post): Post = {
    db.q(
      //      _.selectFrom(POSTS)
      //        .
      _.insertInto(POSTS)
        .values(post.author, post.content, post.creationDate)
        .execute()
    )
    post
  }

  override def findPosts(count: Int): SortedSet[Post] = {

  }
}
