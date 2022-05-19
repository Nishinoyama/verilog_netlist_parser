package jp.ac.nara_k.info.verilog_netlist

import scala.collection.mutable

class StdTopModule(override val netlist_name: String) extends TopModule {
  override val inputs = new mutable.HashSet[InputWire]
  override val outputs = new mutable.HashSet[OutputWire]
  override val wires = new mutable.HashSet[InnerWire]
  override val submodules = new mutable.HashSet[Submodule]
  override val assigns = new mutable.HashSet[Assign]
}
