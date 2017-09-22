package persistencia.tablas.mensajes

import java.sql.Timestamp
import java.time.LocalDateTime
import util.FechaUtil._
import dominio.DocumentType

object MensajeDTO
  extends ((String, String, DocumentType, Option[String], String, LocalDateTime, Option[String], Option[LocalDateTime], String, String, String, Option[LocalDateTime], Option[LocalDateTime]) => MensajeDTO) {

  def apply(t: (String, String, String, Option[String], String, Timestamp, Option[String], Option[Timestamp], String, String, String, Option[Timestamp], Option[Timestamp])): MensajeDTO =
    MensajeDTO(t._1, t._2, DocumentType(t._3), t._4, t._5, timeStamp2LocalDateTime(t._6), t._7, t._8.map(timeStamp2LocalDateTime), t._9, t._10, t._11, t._12.map(timeStamp2LocalDateTime), t._13.map(timeStamp2LocalDateTime))

  def toRow(dto: MensajeDTO): Option[(String, String, String, Option[String], String, Timestamp, Option[String], Option[Timestamp], String, String, String, Option[Timestamp], Option[Timestamp])] = {
    val row = (
      dto.id,
      dto.policy,
      dto.tipoMensaje.toString,
      dto.resultadoValidacion,
      dto.mensajeCore,
      localDateTime2timeStamp(dto.fechaMensajeCore),
      dto.mensajeSap,
      dto.fechaMensajeSap.map(localDateTime2timeStamp),
      dto.ramo,
      dto.cdClase,
      dto.snAnulacion,
      dto.feContabilizacion.map(localDateTime2timeStamp),
      dto.feDocumento.map(localDateTime2timeStamp)
    )
    Option(row)
  }
}

case class MensajeDTO(
                       id: String,
                       policy: String,
                       tipoMensaje: DocumentType,
                       resultadoValidacion: Option[String],
                       mensajeCore: String,
                       fechaMensajeCore: LocalDateTime,
                       mensajeSap: Option[String],
                       fechaMensajeSap: Option[LocalDateTime],
                       ramo: String,
                       cdClase: String = "NA",
                       snAnulacion: String = "N",
                       feContabilizacion: Option[LocalDateTime],
                       feDocumento: Option[LocalDateTime]
                     )
