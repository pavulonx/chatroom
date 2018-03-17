package database

import javax.inject.Inject

import akka.actor.ActorSystem
import org.jooq.{DSLContext, SQLDialect}
import org.jooq.impl.DSL
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

class DB @Inject()(val db: Database, val actorSystem: ActorSystem) {

  val databaseContext: ExecutionContext = actorSystem.dispatchers.lookup("contexts.database")

  def q[A](stmt: DSLContext => A): Future[A] = query(stmt)

  def query[A](stmt: DSLContext => A): Future[A] = Future {
    db.withConnection { connection =>
      val sql = DSL.using(connection, SQLDialect.POSTGRES_9_4)
      stmt(sql)
    }
  }(databaseContext)

  def withTransaction[A](stmt: DSLContext => A): Future[A] = Future {
    db.withTransaction { connection =>
      val sql = DSL.using(connection, SQLDialect.POSTGRES_9_4)
      stmt(sql)
    }
  }(databaseContext)

}
