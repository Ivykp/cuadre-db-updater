package actores

import akka.actor.Actor

case class DocumentoProcesadoOK(id: String)
case object ProcesamientoTerminado
case class DocumentoError(id: String, error: String)

class ContadorMensajesAct extends Actor {
  var contador: Int = 0

  override def receive: Receive = {
    case DocumentoProcesadoOK(id) =>
      println(s"[OK] Documento ${id} procesado")
      contador += 1

    case `ProcesamientoTerminado` =>
      println(s"[RESULTADO] Se procesaron $contador mensajes con exito")

    case DocumentoError(id, error) =>
      println(s"[ERROR] Documento $id presento un error: $error")

  }

}
