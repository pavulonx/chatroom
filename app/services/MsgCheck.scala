package services
import actors.messages.Message

import org.slf4j.{Logger, LoggerFactory}

trait Check[T] {
  def check(t: T): Boolean
}

trait Chain[T, R] {
  def next(t: T): R
}

class CheckChainInitializer[T](chain: Chain[T, Boolean]) extends Check[T] {
  override def check(t: T): Boolean = chain.next(t)
}

abstract class MsgCheck(val nextCheck: Option[MsgCheck]) extends Check[Message] with Chain[Message, Boolean] {

  override def next(msg: Message): Boolean = {
    check(msg) && nextCheck.forall(_.next(msg))
  }

  def check(msg: Message): Boolean

}

class MsgEmptynessCheck(nextCheck: Option[MsgCheck]) extends MsgCheck(nextCheck = nextCheck) {

  override def check(msg: Message): Boolean = Option(msg).map(_.msg).map(_.trim).exists(!_.isEmpty)

}

class MsgLengthCheck(nextCheck: Option[MsgCheck]) extends MsgCheck(nextCheck = nextCheck) {

  override def check(msg: Message): Boolean = msg.msg.length < 255

}

class MsgCharactersCheck(nextCheck: Option[MsgCheck]) extends MsgCheck(nextCheck = nextCheck) {

  override def check(msg: Message): Boolean = msg.msg.forall(_.isLetterOrDigit)

}

class MsgUppercaseFirstCharacterCheck(nextCheck: Option[MsgCheck]) extends MsgCheck(nextCheck = nextCheck) {

  override def check(msg: Message): Boolean = msg.msg.charAt(0).isLetter && msg.msg.charAt(0).isUpper

}

class LoggingCheckDecorator[T](decorated: Check[T]) extends Check[T] {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  override def check(t: T): Boolean = {
    val start = System.currentTimeMillis
    logger.info("Start checking, current checker:[{}]", decorated.getClass.getSimpleName)

    val result = decorated.check(t)

    logger.info("Checking [{}] done with result: {}. Check time: [{} ms]",
      Array(decorated.getClass.getSimpleName, result, System.currentTimeMillis - start))
    result
  }
}

object MsgCheckFactory {

  def default(): Check[Message] = {
    implicit def wrapWithOption(check: MsgCheck): Option[MsgCheck] = Option(check)

    val caseCheck = new MsgUppercaseFirstCharacterCheck(None)
    val charactersCheck = new MsgCharactersCheck(caseCheck)
    val lengthCheck = new MsgLengthCheck(charactersCheck)
    val emptynessCheck = new MsgEmptynessCheck(lengthCheck)

    new LoggingCheckDecorator[Message](new CheckChainInitializer[Message](emptynessCheck))
  }
}
