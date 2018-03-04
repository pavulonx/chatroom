package dao

import model.{Post, User}

import scala.collection.SortedSet

trait ChatDao {
  def save(user: User): User

  def findUser(username: String): Option[User]

  def updateUser(user: User): User

  def deleteUser(username: String): User

  def save(post: Post): Post

  def findPosts(count: Int = 10): SortedSet[Post]

  //  def deletePost(postId: String)

  //  def updatePost(): String
}
