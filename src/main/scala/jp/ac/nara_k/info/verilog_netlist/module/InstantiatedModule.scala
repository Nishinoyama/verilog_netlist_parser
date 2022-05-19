package jp.ac.nara_k.info.verilog_netlist.module

trait InstantiatedModule {
  val input_ports: Iterable[(Identifier, Wire)]
  val output_ports: Iterable[(Identifier, Wire)]
}
