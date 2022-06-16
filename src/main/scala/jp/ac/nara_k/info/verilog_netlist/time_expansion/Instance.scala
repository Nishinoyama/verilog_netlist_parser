package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.ModuleInstance

class Instance(val module_name: String, val portConnections: Seq[PortConnection]) {
  def name: String = module_name

  override def toString: String = s"$module_name (${portConnections.mkString(", ")})"

  def serialized(name: String) = s"$module_name $name (${portConnections.mkString(", ")})"

  def wireConnectedPort(port: String): Option[Wire] = portConnections.find(_.port.equals(port)).map(_.wire)
}

object Instance {
  def apply(module_name: String, portConnections: Seq[PortConnection]): Instance = new Instance(module_name, portConnections)
  def fromAst(instance: ModuleInstance): (String, Instance) = {
    (instance.declaration.ident, new Instance(instance.module_name, PortConnection.fromAstInstant(instance)))
  }
}
