package dominio

sealed trait DocumentType

object DocumentType {
  def apply(c: String): DocumentType = c match {
    case "CD" | "CV" | "FACTURA" => Invoice
    case "CK" | "PAGO" => Payment
    case "CS" | "RESERVA" => Reserve
    case "CB" | "CT" | "REINTEGRO" => Recovery
    case "45" | "PROVISION_COMISION" => Commission
    case _ => InvalidDocument
  }
}

case object Invoice extends DocumentType {
  override def toString: String = "FACTURA"
}

case object Commission extends DocumentType {
  override def toString: String = "PROVISION_COMISION"
}

case object Payment extends DocumentType {
  override def toString: String = "PAGO"
}

case object Reserve extends DocumentType {
  override def toString: String = "RESERVA"
}

case object Recovery extends DocumentType {
  override def toString: String = "REINTEGRO"
}

case object InvalidDocument extends DocumentType {
  override def toString: String = "TIPO_DOCUMENTO_INVALIDO"
}
