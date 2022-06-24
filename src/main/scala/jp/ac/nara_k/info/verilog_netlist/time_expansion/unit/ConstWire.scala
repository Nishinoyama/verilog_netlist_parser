package jp.ac.nara_k.info.verilog_netlist.time_expansion.unit

trait ConstWire extends Wire {
  def constVal: Int
}

object ConstWire {
  def fromInt(const: Int): ConstWire = const match {
    case 0 => Const0Wire
    case 1 => Const1Wire
  }
}
