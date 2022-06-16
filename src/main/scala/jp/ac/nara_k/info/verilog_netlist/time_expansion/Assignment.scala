package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.SingleAssignment

class Assignment(val left_wire: Wire, val right_wire: Wire) {
  override def toString: String = s"$left_wire = $right_wire"
}

object Assignment {
  def fromAst(assignment: SingleAssignment): Assignment =
    new Assignment(Wire.fromAst(assignment.lvalue), Wire.fromAst(assignment.expression))
}
