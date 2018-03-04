package model

import java.time.LocalDateTime

case class User(
                 userId: String,
                 name: String,
                 creationDate: LocalDateTime = LocalDateTime.now,
                 note: String
               ) extends Ordered[User] {

  override def compare(that: User): Int = this.creationDate.compareTo(that.creationDate)
}

case class Post(
                 postId: String,
                 content: String,
                 creationDate: LocalDateTime = LocalDateTime.now,
                 author: User
               ) extends Ordered[Post] {

  override def compare(that: Post): Int = this.creationDate.compareTo(that.creationDate)

}