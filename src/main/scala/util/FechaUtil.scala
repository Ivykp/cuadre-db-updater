package util

import java.sql.Timestamp
import java.time.{ZoneOffset, ZoneId, Instant, LocalDateTime}


object FechaUtil {
  def timeStamp2LocalDateTime(ts: Timestamp): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime), ZoneId.systemDefault())

  def localDateTime2timeStamp(ldt: LocalDateTime): Timestamp =
    new Timestamp(ldt.toInstant(ZoneOffset.ofHours(-5)).toEpochMilli)
}
