package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.{Expression, Number, SingleIdentifier}

class Wire(ident: String)

object Wire {
  def fromAst(singleIdentifier: Expression): Wire = singleIdentifier match {
    case SingleIdentifier(ident) => new Wire(ident)
    case Number(const) => ConstWire.fromInt(const)
  }
}
