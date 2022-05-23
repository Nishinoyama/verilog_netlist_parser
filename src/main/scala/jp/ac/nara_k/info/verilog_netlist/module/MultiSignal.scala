package jp.ac.nara_k.info.verilog_netlist.module

trait MultiSignal extends IdentifiedSignal {
  val range: (Int, Int)

  override def toString: String = s"${this.ident}[${this.range._2}:${this.range._1}]"
}
