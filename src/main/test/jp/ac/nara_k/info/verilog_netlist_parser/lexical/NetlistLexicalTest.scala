package jp.ac.nara_k.info.verilog_netlist_parser.lexical

import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ListBuffer
import scala.util.parsing.input.Reader

class NetlistLexicalTest extends AnyFunSuite {

  private def lex[Lexer <: NetlistLexical](lexer: Lexer, input: String): List[lexer.Token] = {
    var scanner: Reader[lexer.Token] = new lexer.Scanner(input)
    val listBuffer = ListBuffer[lexer.Token]()
    while (!scanner.atEnd) {
      listBuffer.append(scanner.first)
      scanner = scanner.rest
    }
    listBuffer.toList
  }

  test("NetlistLexical.parseKeyword") {
    object Lexer extends NetlistLexical
    Lexer.reserved.add("module")
    import Lexer._
    assert(
      lex(Lexer, "module b04") equals
        List(Keyword("module"), Identifier("b04"))
    )
  }

  test("NetlistLexical.parseDelimiters") {
    object Lexer extends NetlistLexical
    Lexer.delimiters.addAll("()[],.;".flatten[String](c => List(c.toString)))
    import Lexer._
    assert(
      lex(Lexer, " NR2I U10 ( .A(linea), .B(stato[2]), .Z(n11) );") equals
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
    Lexer.delimiters.addAll("()[],.;".flatten[String](c => List(c.toString)))
    import Lexer._
  }
}
