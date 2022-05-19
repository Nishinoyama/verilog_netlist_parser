package jp.ac.nara_k.info.verilog_netlist.parser.input

import jp.ac.nara_k.info.verilog_netlist.parser.token.NetlistTokens.Token

import scala.util.parsing.input.{NoPosition, Position, Reader}

class NetlistTokenReader(tokens: Seq[Token]) extends Reader[Token] {
  override def first: Token = tokens.head

  override def rest: Reader[Token] = new NetlistTokenReader(tokens.tail)

  override def pos: Position = NoPosition

  override def atEnd: Boolean = tokens.isEmpty
}
