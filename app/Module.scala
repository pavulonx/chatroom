import com.google.inject.AbstractModule
import java.time.Clock

import dao._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)

        bind(classOf[RedisConnection]).asEagerSingleton()
        bind(classOf[ChatDao]).to(classOf[RedisChatDao])
//    bind(classOf[ChatDao]).to(classOf[RDChatDao])
  }

}
