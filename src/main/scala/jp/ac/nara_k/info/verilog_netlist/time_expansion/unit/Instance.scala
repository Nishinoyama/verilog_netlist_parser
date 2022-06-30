package jp.ac.nara_k.info.verilog_netlist.time_expansion.unit

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.ModuleInstance
import jp.ac.nara_k.info.verilog_netlist.time_expansion.NetlistModule

class Instance(val module_name: String, val port_connections: Seq[PortConnection]) {
  def name: String = module_name

  override def toString: String = s"$module_name (${port_connections.mkString(", ")})"

  def serialized(name: String) = s"$module_name $name (${port_connections.mkString(", ")})"

  def wireConnectedPort(port: String): Option[Wire] = port_connections.find(_.port.equals(port)).map(_.wire)
}

object Instance {
  def fromNetlistModule(module: NetlistModule): Instance = {
    Instance(module.name, module.inputs.concat(module.outputs).map(PortConnection.fromWire).toSeq)
  }

  def apply(module_name: String, port_connections: Seq[PortConnection]): Instance = new Instance(module_name, port_connections)

  def fromAst(instance: ModuleInstance): (String, Instance) = {
    (instance.declaration.ident, Instance(instance.module_name, PortConnection.fromAstInstant(instance)))
  }
}
