package persistencia.tablas.mensajes

import java.time.LocalDateTime

import dominio.DocumentType
import persistencia.config.OracleDataSource
import com.typesafe.slick.driver.oracle.OracleDriver.api._
import slick.backend.DatabasePublisher
import util.FechaUtil._

import scala.concurrent.Future

object MensajeDAO {
  def getPendingMessagesToUpdate(): DatabasePublisher[MensajeDTO] = {
    OracleDataSource.db.stream(
      TablaMensajeDef.tabla
        .filter(msg => msg.fechaContabilizacion.isEmpty && msg.fechaDocumento.isEmpty && msg.mensajeSap.isDefined)
        .result
    )
  }

  def updateMessage(dto: MensajeDTO, feCont: LocalDateTime, feDoc: LocalDateTime): Future[Int] = {
    OracleDataSource.db.run(
      getDocumentByIdQuery(dto.id, dto.policy, dto.tipoMensaje, dto.snAnulacion, dto.cdClase)
        .map(msg => (msg.fechaContabilizacion, msg.fechaDocumento))
        .update((Some(localDateTime2timeStamp(feCont)), Some(localDateTime2timeStamp(feDoc))))
    )
  }

  private def getDocumentByIdQuery(id: String, policy: String, msgType: DocumentType, annulment: String, msgClass: String) = {
    TablaMensajeDef.tabla
      .filter(msg => msg.id === id && msg.policy === policy && msg.tipoMensaje === msgType.toString && msg.snAnulacion === annulment && msg.cdClase === msgClass)
  }

}

