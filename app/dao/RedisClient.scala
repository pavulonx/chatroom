package dao

import org.redisson.Redisson
import org.redisson.config.{Config, SingleServerConfig}
import javax.inject.{Inject, Singleton}

import org.redisson.codec.SerializationCodec


@Singleton
class RedisClient @Inject()(conn: RedisConnection) extends Redisson(conn.cfg)

@Singleton
class RedisConnection {
  val HOST: String = "172.17.0.3"
  val PORT: String = "6379"

  val cfg = new Config()
  private val ssConfig: SingleServerConfig = cfg.useSingleServer()
  ssConfig.setAddress(s"redis://$HOST:$PORT")
  cfg.setCodec(new SerializationCodec)
}
