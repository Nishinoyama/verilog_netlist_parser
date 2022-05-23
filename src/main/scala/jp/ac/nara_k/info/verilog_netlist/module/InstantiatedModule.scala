package jp.ac.nara_k.info.verilog_netlist.module

trait InstantiatedModule {
  val input_ports: Iterable[(IdentifiedSignal, InnerWire)]
  val output_ports: Iterable[(IdentifiedSignal, InnerWire)]
}
