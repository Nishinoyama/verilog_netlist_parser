package jp.ac.nara_k.info.verilog_netlist.module

trait IdentifiedSignal {
  def ident: String

  override def toString: String = this.ident
}
