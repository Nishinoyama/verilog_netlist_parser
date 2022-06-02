package jp.ac.nara_k.info.verilog_netlist.parser

import jp.ac.nara_k.info.verilog_netlist.graph.SingleAssignmentOnlyModuleGraph
import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst
import jp.ac.nara_k.info.verilog_netlist.parser.input.NetlistTokenReader
import jp.ac.nara_k.info.verilog_netlist.parser.lexical.StdNetlistLexical
import jp.ac.nara_k.info.verilog_netlist.parser.semantic.AnalyzedSingleAssignmentOnlyModule
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class NetlistParsersTest extends AnyFunSuite with NetlistParsers {

  import NetlistAst._

  private def lex(net: String) = {
    object lexical extends StdNetlistLexical
    import lexical._
    parse(tokens, net).getOrElse(List.empty)
  }

  private def genTokenReader(net: String) = {
    new NetlistTokenReader(lex(net))
  }

  private def _test_parser[T](parser: Parser[T], net_excepted: (String, T)): Unit = {
    assert(parser(genTokenReader(net_excepted._1)).get.equals(net_excepted._2))
  }

  private def test_parser[T] = (_test_parser[T] _).curried

  test("NetlistParsers.identifier") {
    test_parser(identifier)("m13.m141.n13", "m13.m141.n13")
    test_parser(indexed_identifier)("datai[12]", IndexedIdentifier("datai", 12))
  }

  test("NetlistParsers.expression") {
    List(
      ("0", Number(0)),
      ("1", Number(1)),
      ("1'b0", Number(0)),
      ("1'b1", Number(1)),
      ("iden", SingleIdentifier("iden")),
      ("idx[3]", IndexedIdentifier("idx", 3)),
    ).foreach(test_parser(expression))
  }

  test("NetlistParsers.parseISCAS_b02") {
    val source = Source.fromFile("b17_net.v")
    val b02_net = source.getLines().mkString("\n")
    source.close()
    val parseResult = module(genTokenReader(b02_net)).get
    val analyzedModule = new AnalyzedSingleAssignmentOnlyModule(parseResult)
    val moduleGraph = new SingleAssignmentOnlyModuleGraph(analyzedModule)
    //    println(moduleGraph)
    println(moduleGraph.search("datai__2"))
    println(moduleGraph.relation("P2_U1283", "P2_U895"))
  }
}
