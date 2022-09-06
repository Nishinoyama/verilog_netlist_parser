package jp.ac.nara_k.info.verilog_netlist.netlist.unit

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.{Expression, Number, SingleIdentifier}

trait Wire {
  override def toString: String = s"$ident"

  def ident: String

  def isConst: Boolean

  def isValid: Boolean

  override def hashCode(): Int = this.ident.hashCode

  override def equals(obj: Any): Boolean = obj match {
    case wire: Wire => wire.ident.equals(this.ident)
    case _ => false
  }
}

object Wire {
  implicit val wireOrdering: Ordering[Wire] = Ordering.by(_.ident)

  def apply(_ident: String): Wire = new NamedWire(_ident)

  def apply(singleIdentifier: Expression): Wire = singleIdentifier match {
    case SingleIdentifier(ident) => new NamedWire(ident)
    case Number(const) => ConstWire(const)
  }

  def scanWires(): Set[Wire] = {
    Set("CLOCK", "clock", "RESET", "reset", "test_se", "test_si", "test_so").map(Wire(_))
  }
}
