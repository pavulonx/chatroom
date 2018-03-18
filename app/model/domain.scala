package model

import java.time.LocalDateTime

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import utils.DomainUtils.randomId

case class User(
                 @JsonProperty("userId")
                 userId: Long = randomId(),

                 @JsonProperty("name")
                 name: String = "",

                 @JsonSerialize(using = classOf[LocalDateTimeSerializer])
                 @JsonDeserialize(using = classOf[LocalDateTimeDeserializer])
                 @JsonProperty("modificationDate")
                 modificationDate: LocalDateTime = LocalDateTime.now,

                 @JsonProperty("note")
                 note: String = ""
               ) extends Ordered[User] {


  override def compare(that: User): Int = this.modificationDate.compareTo(that.modificationDate)
}

case class Post(
                 @JsonProperty("postId")
                 postId: Long = randomId(),

                 @JsonProperty("content")
                 content: String = "",

                 @JsonSerialize(using = classOf[LocalDateTimeSerializer])
                 @JsonDeserialize(using = classOf[LocalDateTimeDeserializer])
                 @JsonProperty("modificationDate")
                 modificationDate: LocalDateTime = LocalDateTime.now,

                 @JsonProperty("author")
                 author: User = null
               ) extends Ordered[Post] {

  override def compare(that: Post): Int = this.modificationDate.compareTo(that.modificationDate)

}