package persistencia.config

import com.typesafe.slick.driver.oracle.OracleDriver
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend

sealed trait DBConfig {

  val profile: JdbcProfile

  val db: JdbcBackend#DatabaseDef
}

/**
  * Instancia de base de datos de Oracle.
  */
object OracleConfig extends DBConfig {
  val profile: JdbcProfile = OracleDriver
  val db: JdbcBackend#DatabaseDef = OracleDataSource.db
}
