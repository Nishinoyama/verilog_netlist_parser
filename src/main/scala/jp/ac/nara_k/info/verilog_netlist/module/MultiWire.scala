package jp.ac.nara_k.info.verilog_netlist.module

class MultiWire(override val ident: String, override val range: (Int, Int)) extends InnerWire(ident) with MultiSignal
