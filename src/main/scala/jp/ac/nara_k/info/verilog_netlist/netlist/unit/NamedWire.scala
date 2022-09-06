package jp.ac.nara_k.info.verilog_netlist.netlist.unit

class NamedWire(_ident: String) extends Wire {
  override def ident: String = _ident

  override def isConst: Boolean = false

  override def isValid: Boolean = true
}
