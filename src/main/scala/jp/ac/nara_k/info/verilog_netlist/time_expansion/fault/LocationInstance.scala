package jp.ac.nara_k.info.verilog_netlist.time_expansion.fault

class LocationInstance(_instance_ident: String, _pin: String) extends Location {
  override def isTop: Boolean = false

  def instanceIdent: String = _instance_ident

  def pin: String = _pin

  override def toString: String = s"$instanceIdent/$pin"
}
