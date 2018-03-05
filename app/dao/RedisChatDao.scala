package dao

import java.util

import collection.JavaConverters._
import javax.inject.{Inject, Singleton}

import model.{Post, User}
import org.redisson.api.{RAtomicLong, RMap, RScoredSortedSet}
import org.redisson.client.RedisException
import scala.compat.java8.FutureConverters._

import scala.collection.SortedSet
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RedisChatDao @Inject()(redisClient: RedisClient) extends ChatDao {

  def userMap: RMap[Long, User] = redisClient.getMap[Long, User](RedisChatDao.USERS_KEY)

  def postsSet: RScoredSortedSet[Post] = redisClient.getScoredSortedSet[Post](RedisChatDao.USERS_KEY)

  def lastIndexOfPost: RAtomicLong = redisClient.getAtomicLong(RedisChatDao.POSTS_INDEX_KEY)

  override def save(user: User): Future[User] = userMap.put(user.userId, user)

  override def findUser(userId: Long): Future[Option[User]] = userMap.getAsync(userId).toScala.map(Option(_))

  override def updateUser(user: User): Future[User] = userMap.putAsync(user.userId, user).toScala

  override def deleteUser(userId: Long): Future[Option[User]] = userMap.removeAsync(userId).toScala.map(Option(_))


  override def save(post: Post): Future[Post] = {
    val newIndex = lastIndexOfPost.incrementAndGet()
    postsSet.add(newIndex, post)
    post
  }

  override def findPosts(count: Int): Future[SortedSet[Post]] = {
    val posts: util.Collection[Post] = postsSet.valueRange(-count, -1)
    posts.asScala.to[SortedSet]
  }

  def failure: Nothing = throw new RedisException
}

object RedisChatDao {
  val USERS_KEY = "users"
  val POSTS_KEY = "posts"
  val POSTS_INDEX_KEY = "postIndex"
}