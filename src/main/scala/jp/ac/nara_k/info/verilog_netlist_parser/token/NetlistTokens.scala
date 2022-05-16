package jp.ac.nara_k.info.verilog_netlist_parser.token

import scala.util.parsing.combinator.token.Tokens

object NetlistTokens extends Tokens {
  case class Keyword(chars: String) extends Token
  case class Identifier(chars: String) extends Token
  case class NumericLit(chars: String) extends Token
}
