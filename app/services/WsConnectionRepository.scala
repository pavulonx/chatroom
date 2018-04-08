package services

import akka.actor.ActorRef
import javax.inject.Singleton

import scala.collection.mutable


@Singleton
class WsConnectionRepository extends mutable.Iterable[ActorRef] {

  private val connectionPool: mutable.Set[ActorRef] = mutable.LinkedHashSet()

  def in(actorRef: ActorRef): Unit = {
    connectionPool += actorRef
  }

  def out(actorRef: ActorRef): Unit = {
    connectionPool -= actorRef
  }

  override def iterator: Iterator[ActorRef] = connectionPool.iterator
}
