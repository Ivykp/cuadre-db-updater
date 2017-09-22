package dominio

sealed trait Currency

object Currency {
  def apply(value: String): Currency = value.toUpperCase match {
    case "USD" => USD
    case "COP" => COP
    case _ => INVALID_CURRENCY
  }
}

case object COP extends Currency {
  override def toString: String = "COP"
}

case object USD extends Currency {
  override def toString: String = "USD"
}

case object INVALID_CURRENCY extends Currency {
  override def toString: String = "MONEDA_INVALIDA"
}

