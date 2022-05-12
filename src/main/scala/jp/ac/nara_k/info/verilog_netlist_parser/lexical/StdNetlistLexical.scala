package jp.ac.nara_k.info.verilog_netlist_parser.lexical

import scala.util.parsing.combinator.token.Tokens

class StdNetlistLexical extends NetlistLexical with Tokens {
  delimiters.addAll("()[],.:;=".flatten[String](c => List(c.toString)))
  reserved.add("module")
  reserved.add("input")
  reserved.add("output")
  reserved.add("wire")
  reserved.add("assign")
  reserved.add("endmodule")
}
