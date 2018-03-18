package model

import java.time.LocalDateTime

import utils.DomainUtils.randomId

case class User(
                 userId: Long = randomId(),
                 name: String = "",
                 modificationDate: LocalDateTime = LocalDateTime.now,
                 note: String = ""
               ) extends Ordered[User] {


  override def compare(that: User): Int = this.modificationDate.compareTo(that.modificationDate)
}

case class Post(
                 postId: Long = randomId(),
                 content: String = "",
                 modificationDate: LocalDateTime = LocalDateTime.now,
                 author: User = null
               ) extends Ordered[Post] {

  override def compare(that: Post): Int = this.modificationDate.compareTo(that.modificationDate)

}