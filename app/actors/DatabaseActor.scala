//package actors
//
//import javax.inject.Inject
//
//import akka.actor.Actor
//import dao.ChatDao
//import model.{Post, User}
//import actors.messages._
//import play.api.Logger
//
//class DatabaseActor @Inject()(val chatDao: ChatDao) extends Actor {
//
//  lazy val logger: Logger = Logger(getClass)
//
//  override def receive: Receive = {
//    case NewMessage(username, msg) =>
//      val user = User(1L, username)
//      chatDao.save(user).flatMap(
//        _ => chatDao.save(Post(1L, msg, author = user))
//      )(context.dispatcher)
//    case RegisterUser(username) =>
//      val user = User(1L, username)
//      chatDao.save(user)
//    case _ => logger.warn("Cannot handle message")
//  }
//}
