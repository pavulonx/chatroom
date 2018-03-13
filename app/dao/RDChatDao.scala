package dao

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId}
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import utils.conversions._
import database.DB
import model.{Post, User}

import collection.JavaConverters._

import scala.collection.SortedSet
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RDChatDao @Inject()(db: DB, val actorSystem: ActorSystem) extends ChatDao {

  implicit val ec: ExecutionContext = actorSystem.dispatchers.lookup("contexts.mapping")

  import generated.Tables._

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

  override def findAllUsers(): Future[Set[User]] = {
    db.q(
      _.selectFrom(USERS)
        .fetchInto(classOf[User])
        .asScala.to[Set]
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
    val maybeUser = findUser(userId)
    db.q(
      _.deleteFrom(USERS)
        .where(USERS.USER_ID eq userId)
        .execute()
    ).flatMap(_ => maybeUser)
  }

  override def save(post: Post): Future[Post] = {
    db.q(
      _.insertInto(POSTS, POSTS.AUTHOR, POSTS.CONTENT, POSTS.CREATION_DATE)
        .values(post.author.userId, post.content, post.creationDate)
        .execute()
    ).map(_ => post)
  }

  override def findPosts(count: Int): Future[SortedSet[Post]] = ???

}
