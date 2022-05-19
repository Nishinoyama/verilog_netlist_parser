package jp.ac.nara_k.info.verilog_netlist.module

trait Assignment {
  val lvalue: Identifier
  val rvalue: Identifier

  override def toString: String = s"$lvalue = $rvalue"
}
