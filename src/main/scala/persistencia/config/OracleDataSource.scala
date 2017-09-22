package persistencia.config

import com.typesafe.config.ConfigFactory
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend

object OracleDataSource {
  lazy val db: JdbcBackend#DatabaseDef = {
    val config = ConfigFactory.load()
    val loaded = DatabaseConfig.forConfig[JdbcProfile]("database", config)
    loaded.db
  }
}
