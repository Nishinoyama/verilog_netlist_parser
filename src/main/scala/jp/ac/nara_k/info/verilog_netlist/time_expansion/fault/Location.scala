package jp.ac.nara_k.info.verilog_netlist.time_expansion.fault

abstract class Location {
  def isTop: Boolean
}

object Location {
  def apply(location_str: String): Location = location_str.split("/") match {
    case Array(top_pin) => new LocationTop(top_pin)
    case Array(instance_ident, pin) => new LocationInstance(instance_ident, pin)
  }
}
