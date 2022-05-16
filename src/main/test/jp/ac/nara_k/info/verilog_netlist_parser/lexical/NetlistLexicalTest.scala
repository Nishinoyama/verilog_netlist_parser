package jp.ac.nara_k.info.verilog_netlist_parser.lexical

import jp.ac.nara_k.info.verilog_netlist_parser.token.NetlistTokens._
import org.scalatest.funsuite.AnyFunSuite

class NetlistLexicalTest extends AnyFunSuite {

  test("NetlistLexical.parseKeyword") {
    object Lexer extends NetlistLexical
    Lexer.reserved.add("module")
    import Lexer._
    assert(
      parse(tokens, "module b04").get equals
        List(Keyword("module"), Identifier("b04"))
    )
  }

  test("NetlistLexical.parseDelimiters") {
    object Lexer extends NetlistLexical
    Lexer.delimiters.addAll("()[],.;".flatten[String](c => List(c.toString)))
    import Lexer._
    assert(
      parse(tokens, " NR2I U10 ( .A(linea), .B(stato[2]), .Z(n11) );").get equals
        List(
          Identifier("NR2I"), Identifier("U10"), Keyword("("),
          Keyword("."), Identifier("A"), Keyword("("), Identifier("linea"), Keyword(")"),
          Keyword(","), Keyword("."), Identifier("B"), Keyword("("), Identifier("stato"), Keyword("["), NumericLit("2"), Keyword("]"), Keyword(")"),
          Keyword(","), Keyword("."), Identifier("Z"), Keyword("("), Identifier("n11"), Keyword(")"),
          Keyword(")"), Keyword(";"),
        )
    )
  }

  test("NetlistLexical.parseComment") {
    object Lexer extends NetlistLexical
    import Lexer._
    assert(parse(tokens, "// comment").get equals List())
    assert(parse(tokens, "/* com\nment */").get equals List())
  }
}
