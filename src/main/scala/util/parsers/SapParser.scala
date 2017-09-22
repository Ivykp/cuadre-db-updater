package util.parsers

import java.time.LocalDateTime

import dominio.{Currency, Dni, InfoSap, SapDetail}
import util.XMLUtil

import scala.concurrent.Future
import scala.util.Try
import scala.xml.{Node, XML}

trait SapParser {
  def getSapInfo(xmlStr: String): InfoSap

  def getIdSafe(xmlStr: String): Option[String]

  def tryF[A](f: => A): Future[A] = {
    Future.fromTry(Try(f) recoverWith {
      case e: Exception => throw new Exception(s"ERROR DESERIALIZANDO XML: ${e.getMessage}")
    })
  }
}

object SapParser extends SapParser {
  def getIdSafe(xmlStr: String): Option[String] = {
    XMLUtil.getNodePathOption(xmlStr, List("Body", "MessageData", "Referencia"))
      .map(_.text)
  }

  def getId(rootNode: Node): String =
    XMLUtil.getFromNode(rootNode, List("Body", "MessageData", "Referencia")).text

  def getPolicy(rootNode: Node): String = {
    XMLUtil.getFromNode(rootNode, List("Body", "MessageData", "Poliza")).text
  }

  def getDetails(rootNode: Node): List[SapDetail] = {
    val entries = XMLUtil.getFromNode(rootNode, List("Body", "MessageData", "RespuestaSAP", "detalle"))
    val extract = (node: Node, key: String) => (node \ key).text

    entries.theSeq.map(e => SapDetail(
      feDocumento = extract(e, "feDocumento"),
      posNro = extract(e, "posNro"),
      posRel = if ((e \ "posRelacion").isEmpty) None else Option(extract(e, "posRelacion")),
      cdRamo = extract(e, "cdRamo"),
      nit = Dni(extract(e, "nit")),
      ptPrimaTotal = extract(e, "ptPrimaTotal").toDouble,
      ptMovimiento = extract(e, "ptMovimiento").toDouble,
      ptReteFuente = extract(e, "ptReteFuente").toDouble,
      ptReteICA = extract(e, "ptReteICA").toDouble,
      ptReteIVA = extract(e, "ptReteIVA").toDouble,
      ptReteOtros = extract(e, "ptReteOtros").toDouble,
      cdClaseDoc = if (extract(e, "cdClaseDoc").isEmpty) throw new Exception("No se encontró cdClaseDoc") else extract(e, "cdClaseDoc"),
      cdMoneda = Currency(extract(e, "cdMoneda")),
      cdNaturaleza = XMLUtil.getFromNodeOption(e, List("cdNaturaleza")).map(_.text),
      dsPosicion = extract(e, "dsPosicion"),
      feContabilizacion = extract(e, "feContabilizacion")
    )).toList
  }

  def getSapInfo(xmlStr: String): InfoSap = {
    val rootNode = XML.loadString(xmlStr)
    import java.time.format.DateTimeFormatter
    import java.time.format.DateTimeFormatterBuilder
    import java.time.temporal.ChronoField
    val baseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatter = new DateTimeFormatterBuilder().append(baseFormatter)
      .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
      .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
      .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
      .toFormatter

    val details = getDetails(rootNode)
    InfoSap(
      id = getId(rootNode).toUpperCase,
      policyNumber = getPolicy(rootNode),
      details = details,
      doctype = details.head.cdClaseDoc,
      currency = details.head.cdMoneda,
      policyLine = details.head.cdRamo,
      annulment = details.head.cdNaturaleza.contains("H") && details.head.dsPosicion.matches("^([d|D]22).*"),
      documentDate = LocalDateTime.parse(details.head.feDocumento, formatter),
      accountingDate = LocalDateTime.parse(details.head.feContabilizacion, formatter)
    )
  }
}

