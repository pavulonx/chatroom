package dao

import javax.inject.Inject

import model.{Post, User}

import scala.collection.SortedSet

class RDChatDao extends ChatDao {

  override def save(user: User): User = ???

  override def findUser(username: String): Option[User] = ???

  override def updateUser(user: User): User = ???

  override def deleteUser(username: String): Unit = ???

  override def save(post: Post): Post = ???

  override def findPosts(count: Int): SortedSet[Post] = ???
}
