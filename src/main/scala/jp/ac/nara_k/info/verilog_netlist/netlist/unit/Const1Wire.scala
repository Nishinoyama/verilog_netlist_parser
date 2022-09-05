package jp.ac.nara_k.info.verilog_netlist.netlist.unit

object Const1Wire extends Wire("1") with ConstWire {
  override def constVal: Int = 1
}
