package utils

import java.util.Optional

object conversions {

  implicit final class WrapOptional[T](val optional: Optional[T]) {

    def asScala(): Option[T] = optional match {
      case null => null
      case x => if (x.isPresent) Option(x get) else None
    }
  }

  implicit final class WrapOption[T](val optional: Option[T]) {

    def asJava(): Optional[T] = optional match {
      case null => null
      case Some(x) => Optional.of(x)
      case None => Optional.empty()
    }
  }

}
