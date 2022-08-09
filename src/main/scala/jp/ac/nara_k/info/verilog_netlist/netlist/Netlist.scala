package jp.ac.nara_k.info.verilog_netlist.netlist

abstract class Netlist {
  def top_module: NetlistModule

  def sub_modules: Seq[NetlistModule]
}
