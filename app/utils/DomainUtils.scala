package utils

import java.util.UUID

object DomainUtils {

  def randomId(): Long = UUID.randomUUID().hashCode()

}

