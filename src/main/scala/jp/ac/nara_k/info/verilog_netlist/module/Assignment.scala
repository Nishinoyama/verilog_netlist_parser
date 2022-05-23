package jp.ac.nara_k.info.verilog_netlist.module

class Assignment(val lvalue: IdentifiedSignal, val rvalue: IdentifiedSignal) {
  override def toString: String = s"$lvalue = $rvalue"
}
