package dao

import collection.JavaConverters._
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import model.{Post, User}
import org.redisson.api.{RAtomicLong, RMap, RScoredSortedSet}
import org.redisson.client.RedisException

import scala.compat.java8.FutureConverters._
import scala.collection.SortedSet
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RedisChatDao @Inject()(redisClient: RedisClient, actorSystem: ActorSystem) extends ChatDao {

  implicit val ec: ExecutionContext = actorSystem.dispatchers.lookup("contexts.mapping")

  def userMap: RMap[Long, User] = redisClient.getMap[Long, User](RedisChatDao.USERS_KEY)

  def postsSet: RScoredSortedSet[Post] = redisClient.getScoredSortedSet[Post](RedisChatDao.USERS_KEY)

  def lastIndexOfPost: RAtomicLong = redisClient.getAtomicLong(RedisChatDao.POSTS_INDEX_KEY)

  override def save(user: User): Future[User] = userMap.putAsync(user.userId, user).toScala.map(_ => user)

  override def findUser(userId: Long): Future[Option[User]] = userMap.getAsync(userId).toScala.map(Option(_))

  override def findUser(username: String): Future[Option[User]] = userMap.readAllValuesAsync().toScala.map(c => c.asScala.find(_.name == username))

  override def findAllUsers(): Future[Set[User]] = userMap.readAllMapAsync().toScala.map(_.values.asScala.to[Set])

  override def updateUser(user: User): Future[User] = userMap.putAsync(user.userId, user).toScala

  override def deleteUser(userId: Long): Future[Option[User]] = userMap.removeAsync(userId).toScala.map(Option(_))

  override def save(post: Post): Future[Post] = {
    val newIndex = lastIndexOfPost.incrementAndGet()
    postsSet.addAsync(newIndex, post).toScala
      .map(_ => post)
  }

  override def findPosts(count: Int): Future[SortedSet[Post]] = {
    postsSet.valueRangeAsync(-count, -1).toScala.map(
      _.asScala.to[SortedSet]
    )
  }

  def failure: Nothing = throw new RedisException
}

object RedisChatDao {
  val USERS_KEY = "users"
  val POSTS_KEY = "posts"
  val POSTS_INDEX_KEY = "postIndex"
}