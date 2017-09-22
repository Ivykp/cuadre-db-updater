package persistencia.tablas.mensajes

import java.sql.Timestamp

import com.typesafe.slick.driver.oracle.OracleDriver.api._
import slick.lifted.{PrimaryKey, ProvenShape}

object TablaMensajeDef {

  case class TablaMensajes(tag: Tag) extends Table[MensajeDTO](tag, "TRTD_MENSAJES") {

    def pk: PrimaryKey = primaryKey("MENSAJES_PK", (policy, id, cdClase, tipoMensaje, snAnulacion))

    def id: Rep[String] = column[String]("CDMOVIMIENTO", O.Length(_50))

    def cdClase: Rep[String] = column[String]("CDCLASE", O.Length(_5))

    def snAnulacion: Rep[String] = column[String]("SNANULACION", O.Length(_1))

    def policy: Rep[String] = column[String]("CDPOLIZA", O.Length(_20))

    def tipoMensaje: Rep[String] = column[String]("DSTIPO_MENSAJE", O.Length(_20))

    def resultadoValidacion: Rep[Option[String]] = column[Option[String]]("DSRESULTADO_VALIDACION", O.Length(_500))

    def mensajeCore: Rep[String] = column[String]("DSMENSAJE_CORE", O.SqlType("CLOB"))

    def fechaMensajeCore: Rep[Timestamp] = column[Timestamp]("FEMENSAJE_CORE")

    def mensajeSap: Rep[Option[String]] = column[Option[String]]("DSMENSAJE_SAP", O.SqlType("CLOB"))

    def fechaMensajeSap: Rep[Option[Timestamp]] = column[Option[Timestamp]]("FEMENSAJE_SAP")

    def fechaContabilizacion: Rep[Option[Timestamp]] = column[Option[Timestamp]]("FECONTABILIZACION")

    def fechaDocumento: Rep[Option[Timestamp]] = column[Option[Timestamp]]("FEDOCUMENTO")

    def ramo: Rep[String] = column[String]("CDRAMO", O.Length(_500))

    override def * : ProvenShape[MensajeDTO] =
      (id, policy, tipoMensaje, resultadoValidacion, mensajeCore, fechaMensajeCore, mensajeSap, fechaMensajeSap, ramo, cdClase, snAnulacion, fechaContabilizacion, fechaDocumento)
        .<>[MensajeDTO, (String, String, String, Option[String], String, Timestamp, Option[String], Option[Timestamp], String, String, String, Option[Timestamp], Option[Timestamp])](
        MensajeDTO(_),
        MensajeDTO.toRow
      )

    private val _1 = 1
    private val _5 = 5
    private val _20 = 20
    private val _50 = 50
    private val _500 = 500
  }

  val tabla = TableQuery[TablaMensajes]
}

