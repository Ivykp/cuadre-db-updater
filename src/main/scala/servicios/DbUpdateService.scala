package servicios

import actores.{DocumentoError, DocumentoProcesadoOK}
import akka.actor.ActorRef
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import persistencia.tablas.mensajes.{MensajeDAO, MensajeDTO}
import util.PIHelper
import util.parsers.SapParser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

trait DbUpdateService {
  def updateMissingFieldsDB(): Future[Any]
}

sealed trait ResultadoActualizacionMsg
case class MensajeProcesadoAct(result: Int, dto: MensajeDTO) extends ResultadoActualizacionMsg
case class MensajeError(error: String, dto: MensajeDTO) extends ResultadoActualizacionMsg

case class UpdaterService(msgReporter: ActorRef, implicit val mat: ActorMaterializer, implicit val ec: ExecutionContext) extends DbUpdateService {

  private def updateMessage(dto: MensajeDTO): Future[ResultadoActualizacionMsg] = {
    val sapSrc = dto.mensajeSap.getOrElse("")
    val f = for {
      sapInfo <- SapParser.tryF(SapParser.getSapInfo(sapSrc))
      updateRes <- MensajeDAO.updateMessage(dto, sapInfo.accountingDate, sapInfo.documentDate)
    } yield updateRes

    f map { r =>
      MensajeProcesadoAct(r, dto)
    } recover {
      case e: Exception => MensajeError(e.getMessage, dto)
    }
  }

  private def reportUpdateResult(res: ResultadoActualizacionMsg): Unit = {
    res match {
      case MensajeProcesadoAct(result, dto) => msgReporter ! DocumentoProcesadoOK(dto.id)
      case MensajeError(error, dto) => msgReporter ! DocumentoError(dto.id, error)
    }
  }

  def updateMissingFieldsDB(): Future[Any] = {
    val source = Source.fromPublisher(MensajeDAO.getPendingMessagesToUpdate())
    val filterPI = Flow[MensajeDTO].filterNot(msg => PIHelper.isPiErrorResult(msg.mensajeSap.getOrElse("")))
    val updateMsg = Flow[MensajeDTO].mapAsync[ResultadoActualizacionMsg](5)(updateMessage)
    val update = Sink.foreach(reportUpdateResult)
    val runFlow = (source via filterPI via updateMsg).toMat(update)(Keep.right)
    runFlow.run()
  }
}
