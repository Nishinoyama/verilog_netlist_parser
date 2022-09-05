package jp.ac.nara_k.info.verilog_netlist.time_expansion.fault

sealed abstract class SlowTo(_alpha: Int) {
  def alpha: Int = _alpha

  override def toString: String = this match {
    case SlowTo.Rise => "str"
    case SlowTo.Fall => "stf"
  }
}

object SlowTo {
  case object Rise extends SlowTo(_alpha = 0)

  case object Fall extends SlowTo(_alpha = 1)

  def apply(st: String): SlowTo = st.toLowerCase match {
    case "str" => Rise
    case "stf" => Fall
  }
}
