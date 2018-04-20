package dao

import model.{Post, User}

import scala.collection.SortedSet
import scala.concurrent.Future

trait ChatDao {

  def save(user: User): Future[User]

  def findUser(userId: Long): Future[Option[User]]

  def findUser(username: String): Future[Option[User]]

  def findAllUsers(): Future[Set[User]]

  def updateUser(user: User): Future[User]

  def deleteUser(userId: Long): Future[Option[User]]

  def save(post: Post): Future[Post]

  def findPosts(count: Int = 10): Future[SortedSet[Post]]

}
