package dao

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId}
import javax.inject.{Inject, Singleton}
import utils.conversions._

import database.DB
import model.{Post, User}

import scala.collection.SortedSet
import scala.concurrent.{Await, Future}

@Singleton
class RDChatDao @Inject()(db: DB) extends ChatDao {

  import generated.Tables._
  import org.jooq.impl.DSL

  implicit def toTimestamp(dateTime: LocalDateTime): Timestamp = Timestamp.valueOf(dateTime)

  implicit def fromTimestamp(timestamp: Timestamp): LocalDateTime = timestamp.toLocalDateTime

  override def save(user: User): Future[User] = {
    db.q(
      _.insertInto(USERS, USERS.NAME, USERS.NOTE, USERS.CREATION_DATE)
        .values(user.name, user.note, user.creationDate)
        .execute()
    ).map(_ => user)
  }

  override def findUser(userId: Long): Future[Option[User]] = {
    db.q(
      _.selectFrom(USERS).where(USERS.USER_ID eq userId)
        .fetchOptionalInto(classOf[User]).asScala()
    )
  }

  override def updateUser(user: User): Future[User] = {
    db.q(
      _.update(USERS)
        .set(USERS.NAME, user.name)
        .set(USERS.NOTE, user.note)
        .where(USERS.USER_ID eq user.userId)
        .execute()
    ).map(_ => user) //TODO: return real user
  }

  override def deleteUser(userId: Long): Future[Option[User]] = {
    val maybeUser = findUser(uId)
    db.q(
      _.deleteFrom(USERS)
        .where(USERS.NAME eq username)
        .execute()
    ).flatMap(_ => maybeUser)
  }

  override def save(post: Post): Future[Post] = {
    db.q(
      _.insertInto(POSTS)
        .values(post.author, post.content, post.creationDate)
        .execute()
    ).map(_ => post)
  }

  override def findPosts(count: Int): SortedSet[Post] = ???

}
