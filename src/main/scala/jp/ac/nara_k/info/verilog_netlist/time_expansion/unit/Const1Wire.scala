package jp.ac.nara_k.info.verilog_netlist.time_expansion.unit

object Const1Wire extends Wire("1") with ConstWire {
  override def constVal: Int = 1
}
