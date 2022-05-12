package jp.ac.nara_k.info.verilog_netlist_parser.token

trait NetlistTokens extends scala.util.parsing.combinator.token.Tokens {
  case class Keyword(chars: String) extends Token
  case class Identifier(chars: String) extends Token
  case class NumericLit(chars: String) extends Token
}
