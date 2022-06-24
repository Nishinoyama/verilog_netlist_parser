package jp.ac.nara_k.info.verilog_netlist.time_expansion.unit

object UnusedWire extends Wire("**UNUSED**") {
  override def ident: String = throw new Exception("Unused Wire detected")

  override def isConst: Boolean = true
}
