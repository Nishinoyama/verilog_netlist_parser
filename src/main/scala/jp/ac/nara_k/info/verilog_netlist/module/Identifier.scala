package jp.ac.nara_k.info.verilog_netlist.module

trait Identifier {
  val ident: String
  override def toString: String = this.ident
}
