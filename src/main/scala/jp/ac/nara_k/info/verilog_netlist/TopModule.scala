package jp.ac.nara_k.info.verilog_netlist

import jp.ac.nara_k.info.verilog_netlist.module._

trait TopModule {
  val netlist_name: String

  type Input = InputWire
  type Output = OutputWire
  type Wire = InnerWire
  type Submodule = InstantiatedModule
  type Assign = Assignment

  val inputs: Iterable[Input]
  val outputs: Iterable[Output]
  val wires: Iterable[Wire]
  val submodules: Iterable[Submodule]
  val assigns: Iterable[Assign]

  override def toString: String = {
    s"""Netlist $netlist_name [
       |  input  $inputs,
       |  output $outputs,
       |  wire   $wires,
       |  assign $assigns,
       |  module $submodules,
       |]""".stripMargin
  }
}
