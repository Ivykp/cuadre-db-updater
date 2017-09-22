import actores.{ContadorMensajesAct, ProcesamientoTerminado}
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import servicios.UpdaterService
import scala.concurrent.ExecutionContext

object Main extends App {
  implicit lazy val system: ActorSystem = ActorSystem("cuadre-updater")
  implicit lazy val materializer: ActorMaterializer = ActorMaterializer()
  implicit lazy val futuresEc: ExecutionContext = system.dispatchers.lookup("futures-dispatcher")
  val reporter = system.actorOf(Props[ContadorMensajesAct], "contador")

  val f = UpdaterService(reporter, materializer, futuresEc).updateMissingFieldsDB()

  f onFailure  {
    case e: Exception =>
      println(s"[ERROR] Hubo un error procesando documentos - ${e.getMessage}")
  }

  f onSuccess  {
    case _ =>
      reporter ! ProcesamientoTerminado
  }

  f onComplete { _ =>
    system.terminate()
  }

}
