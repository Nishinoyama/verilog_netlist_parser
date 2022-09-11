package jp.ac.nara_k.info.verilog_netlist.netlist.unit

trait ConstWire extends Wire {
  def constVal: Int

  override def isConst: Boolean = true

  override def isValid: Boolean = true
}

object ConstWire {
  def apply(const: Int): ConstWire = const match {
    case 0 => Const0Wire
    case 1 => Const1Wire
  }
}
