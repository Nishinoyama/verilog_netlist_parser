package jp.ac.nara_k.info.verilog_netlist.parser.semantic

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst._

abstract class AnalyzedModule[D<:Declaration, A<:Assignment, M<:ModuleInstance] {
  val inputs: Set[D]

  val outputs: Set[D]

  val wires: Set[D]

  val assignments: Set[A]

  val instantiated_modules: Set[M]
}
