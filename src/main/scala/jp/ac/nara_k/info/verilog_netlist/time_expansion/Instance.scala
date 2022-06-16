package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.ModuleInstance

class Instance(module_name: String, portConnections: Seq[PortConnection]) {
  def name: String = module_name
}

object Instance {
  def fromAst(instance: ModuleInstance): (String, Instance) = {
    (instance.declaration.ident, new Instance(instance.module_name, PortConnection.fromAstInstant(instance)))
  }
}
