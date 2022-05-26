package jp.ac.nara_k.info.verilog_netlist.graph

trait NetlistNode[Label] {
  def label: Label

  override def toString: String = this.label.toString
}
