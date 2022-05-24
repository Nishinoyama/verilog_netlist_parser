package jp.ac.nara_k.info.verilog_netlist.module

class InstantiatedModule
(
  val ident_name: String,
  val module_name: String,
  val input_ports: List[(String, String)],
  val output_ports: List[(String, String)],
) {
  override def toString: String = {
    s"$ident_name $module_name(${input_ports.concat(output_ports).map(a => s".${a._1}.(${a._2})").mkString(",")})"
  }
}
