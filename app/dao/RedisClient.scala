package dao

import org.redisson.Redisson
import org.redisson.config.{Config, SingleServerConfig}

class RedisClient(private[this] val cfg: Config) extends Redisson(cfg) {

  this () {
    this (new Config())
    val config: SingleServerConfig = cfg.useSingleServer()
    config.setAddress(s"${RedisClient.HOST}:${RedisClient.PORT}")
  }

}

object RedisClient {
  val HOST: String = "172.17.0.3"
  val PORT: String = "6379"
}