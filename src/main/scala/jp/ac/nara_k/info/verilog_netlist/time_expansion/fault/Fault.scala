package jp.ac.nara_k.info.verilog_netlist.time_expansion.fault

abstract class Fault(fault_str: String) {
  private val Array(_slow_to, _, _location) = fault_str.split("\\s+", 3)
  def slowTo: SlowTo = SlowTo(_slow_to)
  def location: Location = Location(_location)
  def alpha: Int = slowTo.alpha
  def isStr: Boolean = alpha == 1
  def slowToString: String = if(isStr) "STR" else "STF"
}
