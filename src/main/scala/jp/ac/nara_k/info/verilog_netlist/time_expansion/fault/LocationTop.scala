package jp.ac.nara_k.info.verilog_netlist.time_expansion.fault

class LocationTop(_pin: String) extends Location {
  override def isTop: Boolean = true
  def pin: String = _pin
}
