package jp.ac.nara_k.info.verilog_netlist.module

class MultiOutputWire(override val ident: String, override val range: (Int, Int)) extends OutputWire(ident) with MultiSignal
