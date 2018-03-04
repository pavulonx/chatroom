package dao

import java.util

import collection.JavaConverters._
import javax.inject.Inject

import model.{Post, User}
import org.redisson.api.{RAtomicLong, RMap, RScoredSortedSet}
import org.redisson.client.RedisException

import scala.collection.SortedSet

//@Singleton
class RedisChatDao @Inject()(redisClient: RedisClient) extends ChatDao {

  def userMap: RMap[Long, User] = redisClient.getMap[Long, User](RedisChatDao.USERS_KEY)

  def postsSet: RScoredSortedSet[Post] = redisClient.getScoredSortedSet[Post](RedisChatDao.USERS_KEY)

  def lastIndexOfPost: RAtomicLong = redisClient.getAtomicLong(RedisChatDao.POSTS_INDEX_KEY)

  override def save(user: User): User = userMap.put(user.userId, user)

  override def findUser(username: String): Option[User] = Option(userMap.get(username))

  override def updateUser(user: User): User = userMap.put(user.userId, user)

  override def deleteUser(username: String): User = userMap.remove(username)

  override def save(post: Post): Post = {
    val newIndex = lastIndexOfPost.incrementAndGet()
    postsSet.add(newIndex, post)
    post
  }

  override def findPosts(count: Int): SortedSet[Post] = {
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