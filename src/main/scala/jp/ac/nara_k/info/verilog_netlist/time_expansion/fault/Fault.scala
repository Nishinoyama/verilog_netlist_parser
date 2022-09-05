package jp.ac.nara_k.info.verilog_netlist.time_expansion.fault

class Fault(fault_str: String) {
  private val Array(_slow_to, _fault_class, _location) = fault_str.split("\\s+", 3)

  def slowTo: SlowTo = SlowTo(_slow_to)

  def location: Location = Location(_location)

  def alpha: Int = slowTo.alpha

  def isStr: Boolean = slowTo == SlowTo.Rise

  def slowToString: String = slowTo.toString

  def faultClass: String = _fault_class

  override def toString: String = s"$slowTo $faultClass $location"
}

object Fault {
  def apply(fault_str: String): Fault = new Fault(fault_str)
}
