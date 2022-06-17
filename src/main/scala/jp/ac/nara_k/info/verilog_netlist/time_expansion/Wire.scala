package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.{Expression, Number, SingleIdentifier}

class Wire(_ident: String) {
  override def toString: String = s"$ident"

  def ident: String = _ident

  override def hashCode(): Int = this.ident.hashCode

  override def equals(obj: Any): Boolean = obj match {
    case wire: Wire => wire.ident.equals(this.ident)
    case _ => false
  }
}

object Wire {
  implicit val wireOrdering: Ordering[Wire] = Ordering.by(_.ident)

  def apply(_ident: String): Wire = new Wire(_ident)

  def fromAst(singleIdentifier: Expression): Wire = singleIdentifier match {
    case SingleIdentifier(ident) => new Wire(ident)
    case Number(const) => ConstWire.fromInt(const)
  }

  def scanWires(): Set[Wire] = {
    Set("CLOCK", "clock", "RESET", "reset", "test_se", "test_si", "test_so").map(Wire(_))
  }
}
