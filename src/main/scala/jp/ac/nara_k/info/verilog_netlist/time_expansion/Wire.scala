package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.{Expression, Number, SingleIdentifier}

class Wire(_ident: String) {
  override def toString: String = s"$ident"

  def ident: String = _ident
}

object Wire {
  implicit val wireOrdering: Ordering[Wire] = Ordering.by(_.ident)

  def fromAst(singleIdentifier: Expression): Wire = singleIdentifier match {
    case SingleIdentifier(ident) => new Wire(ident)
    case Number(const) => ConstWire.fromInt(const)
  }
}