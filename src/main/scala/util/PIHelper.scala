package util

object PIHelper {
  def isPiErrorResult(xmlStr: String): Boolean = {
    val isThereResponse = XMLUtil.getNodePathOption(xmlStr, List("Body", "MessageData", "RespuestaSAP")).isEmpty
    val isThereError = XMLUtil.getNodePathOption(xmlStr, List("Header", "Fault", "FaultString")).nonEmpty
    val isError = XMLUtil.getNodePathOption(xmlStr, List("Header", "MessageStatus"))
      .map(node => node.text.toUpperCase)
      .getOrElse("") == "ERROR"

    isThereError && isThereResponse && isError
  }
}
