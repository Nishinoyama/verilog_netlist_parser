package jp.ac.nara_k.info.verilog_netlist.parser.lexical

class StdNetlistLexical extends NetlistLexical {
  delimiters.addAll("()[],.:;=".flatten[String](c => List(c.toString)))
  reserved.addAll(List("module", "input", "output", "wire", "assign", "endmodule"))
}
