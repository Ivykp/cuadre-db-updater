package util

import scala.xml.{NodeSeq, XML}

object XMLUtil {
  val LLAVE_FALTANTE = "Clave no encontrada: key"
  def extractValue(plainXml: String): Seq[String] => String = { baseKeys: Seq[String] =>
    val xml = XML.loadString(plainXml)

    def fun(xml: NodeSeq, keys: Seq[String]): String = {
      keys match {
        case head :: Nil =>
          val value = xml \ head
          if (value.isEmpty) {
            throw new Exception(LLAVE_FALTANTE.replace("key", baseKeys.mkString("/")))
          }
          value.text
        case head :: tail => fun(xml \ head, tail)
      }
    }
    fun(xml, baseKeys)
  }

  def getNodePathOption(plainXml: String, path: List[String]): Option[NodeSeq] = {
    val xml = XML.loadString(plainXml)
    val node: NodeSeq = path.tail.foldLeft(xml \ path.head) {
      (acc, next) => acc \ next
    }

    if (node.isEmpty) {
      None
    } else {
      Option(node)
    }
  }

  def getNodePath(plainXml: String, path: List[String]): NodeSeq = {
    val xml = XML.loadString(plainXml)
    val node: NodeSeq = path.tail.foldLeft(xml \ path.head) {
      (acc, next) => acc \ next
    }

    if (node.isEmpty) {
      throw new Exception(LLAVE_FALTANTE.replace("key", path.last))
    } else {
      node
    }
  }

  def getFromNode(currentNode: NodeSeq, path: List[String]): NodeSeq = {
    val newNode = path.foldLeft(currentNode) {
      (resNode, nextNode) => resNode \ nextNode
    }

    if (newNode.isEmpty) {
      throw new Exception(LLAVE_FALTANTE.replace("key", path.mkString("/")))
    } else {
      newNode
    }
  }

  def getFromNodeOption(currentNode: NodeSeq, path: List[String]): Option[NodeSeq] = {
    val newNode = path.foldLeft(currentNode) {
      (resNode, nextNode) => resNode \ nextNode
    }

    if (newNode.isEmpty) {
      None
    } else {
      Option(newNode)
    }

  }

  def extractPathValues(plainXml: String, path: List[String]): (String) => String = { key: String =>
    val nodePath = getNodePath(plainXml, path)
    val result = nodePath.head \ key
    if (result.nonEmpty) result.text else throw new Exception(LLAVE_FALTANTE.replace("key", key))
  }
}

