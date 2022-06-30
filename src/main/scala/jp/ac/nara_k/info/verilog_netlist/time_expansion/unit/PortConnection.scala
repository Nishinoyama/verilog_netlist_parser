package jp.ac.nara_k.info.verilog_netlist.time_expansion.unit

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.{Expression, ModuleInstance}

class PortConnection(val port: String, _wire: Option[Wire]) {
  def wire: Wire = _wire.getOrElse(UnusedWire)

  def wireConnected(wire: Wire): PortConnection = PortConnection(this.port, wire)

  override def toString: String = s".$port($wire)"
}

object PortConnection {
  def apply(port: String, wire: Wire): PortConnection = new PortConnection(port, Some(wire))

  def apply(port: String): PortConnection = new PortConnection(port, None)

  def fromWire(wire: Wire): PortConnection = PortConnection(wire.ident)

  def fromAstInstant(instance: ModuleInstance): Seq[PortConnection] = {
    instance.port_connections.map(fromPortAst)
  }

  def fromPortAst(portAst: (String, Expression)): PortConnection = {
    new PortConnection(portAst._1, Some(Wire(portAst._2)))
  }
}
