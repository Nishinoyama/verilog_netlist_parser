package jp.ac.nara_k.info.verilog_netlist.parser.semantic

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst._

object ArrayedIndexedIntoSingleIdentifier {

  private def convertedSingle(ident: String, index: Int): SingleIdentifier = {
    SingleIdentifier(ident + s"__$index")
  }

  def convert(identifier: Identifier): Iterable[SingleIdentifier] = identifier match {
    case ArrayedIdentifier(ident, range) => (range._1 to range._2).map(convertedSingle(ident, _))
    case IndexedIdentifier(ident, index) => List(convertedSingle(ident, index))
    case ident: SingleIdentifier => List(ident)
  }

  def convertNonArrayed(identifier: Identifier): SingleIdentifier = identifier match {
    case IndexedIdentifier(ident, index) => convertedSingle(ident, index)
    case ident: SingleIdentifier => ident
  }

  def convertExpression(expression: Expression): Expression = expression match {
    case IndexedIdentifier(ident, index) => convertedSingle(ident, index)
    case x => x
  }

  def convertAll(identifiers: Iterable[Identifier]): Iterable[SingleIdentifier] = {
    identifiers flatMap convert
  }
}
