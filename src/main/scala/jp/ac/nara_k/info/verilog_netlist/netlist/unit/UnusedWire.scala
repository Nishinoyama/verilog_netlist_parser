package jp.ac.nara_k.info.verilog_netlist.netlist.unit

object UnusedWire extends Wire("**UNUSED**") {
  override def ident: String = throw new Exception("Unused Wire detected")

  override def isValid: Boolean = false

  override def isConst: Boolean = false
}
