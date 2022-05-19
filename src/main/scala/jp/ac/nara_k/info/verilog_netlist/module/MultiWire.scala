package jp.ac.nara_k.info.verilog_netlist.module

trait MultiWire extends Wire {
  val range: (Int, Int)

  override def toString: String = s"${super.toString}[${this.range._2}:${this.range._1}]"
}
