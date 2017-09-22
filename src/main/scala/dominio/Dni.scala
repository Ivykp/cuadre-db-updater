package dominio

case class Dni(src: String) {
  val normalized: String = Dni.parseDni(src)
  override def equals(obj: scala.Any): Boolean = obj match {
    case dni: Dni => dni.src == src || dni.normalized == normalized
    case _ => false
  }

  override def toString: String = src
}

object Dni {
  private def parseDni(nit: String): String =
    nit.replaceAll("[a-zA-Z\\s-]", "")
}
