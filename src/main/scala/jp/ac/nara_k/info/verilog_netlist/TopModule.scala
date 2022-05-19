package jp.ac.nara_k.info.verilog_netlist

import jp.ac.nara_k.info.verilog_netlist.module._

trait TopModule {
  val netlist_name: String

  type InputWire = Input
  type OutputWire = Output
  type InnerWire = Wire
  type Submodule = InstantiatedModule
  type Assign = Assignment

  val inputs: Iterable[InputWire]
  val outputs: Iterable[OutputWire]
  val wires: Iterable[InnerWire]
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