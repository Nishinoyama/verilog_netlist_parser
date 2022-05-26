package jp.ac.nara_k.info.verilog_netlist.module

import scala.collection.mutable

class StdTopModule(override val netlist_name: String) extends TopModule {
  override val inputs = new mutable.HashSet[Input]
  override val outputs = new mutable.HashSet[Output]
  override val wires = new mutable.HashSet[Wire]
  override val submodules = new mutable.HashSet[Submodule]
  override val assigns = new mutable.HashSet[Assign]
}
