package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.{Expression, ModuleInstance}

class PortConnection(port: String, wire: Wire) {
  override def toString: String = s".$port($wire)"
}

object PortConnection {
  def fromAstInstant(instance: ModuleInstance): Seq[PortConnection] = {
    instance.port_connections.map(fromPortAst)
  }

  def fromPortAst(portAst: (String, Expression)): PortConnection = {
    new PortConnection(portAst._1, Wire.fromAst(portAst._2))
  }
}
