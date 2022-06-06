package jp.ac.nara_k.info.verilog_netlist.parser.semantic

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.Expression

trait AnalyzedModule {
  def inputs: Iterable[String]

  def outputs: Iterable[String]

  def wires: Iterable[String]

  def assignments: Iterable[(String, Expression)]

  def instantiatedModules: Iterable[(String, (String, Iterable[(String, Expression)]))]
}
