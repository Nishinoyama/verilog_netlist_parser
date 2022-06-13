package jp.ac.nara_k.info.verilog_netlist.time_expansion

object Const0Wire extends Wire("0") with ConstWire {
  override def constVal: Int = 0
}
