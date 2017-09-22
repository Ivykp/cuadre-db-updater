package dominio

import java.time.LocalDateTime

case class InfoSap(
                    id: String,
                    policyNumber: String,
                    details: List[SapDetail],
                    currency: Currency,
                    doctype: String,
                    policyLine: String = "DESCONOCIDO",
                    documentDate: LocalDateTime,
                    annulment: Boolean,
                    accountingDate: LocalDateTime
                  )

case class SapDetail(
                      posNro: String,
                      posRel: Option[String],
                      cdRamo: String,
                      nit: Dni,
                      ptPrimaTotal: Double,
                      ptMovimiento: Double,
                      ptReteFuente: Double,
                      ptReteICA: Double,
                      ptReteIVA: Double,
                      ptReteOtros: Double,
                      cdMoneda: Currency,
                      cdClaseDoc: String,
                      feDocumento: String,
                      cdNaturaleza: Option[String],
                      dsPosicion: String,
                      feContabilizacion: String
                    )

