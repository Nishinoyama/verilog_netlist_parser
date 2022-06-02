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

  private def _test_parser[T](parser: Parser[T], net_excepted: (String, T)): Boolean = {
    val parsed = parser(genTokenReader(net_excepted._1))
    if (parsed.successful) {
      assert(parsed.get.equals(net_excepted._2), parsed.get + " != " + net_excepted._2)
      true
    } else {
      false
    }
  }

  private def test_parser[T] = (_test_parser[T] _).curried

  private def test_parser_for_each[T](parser: Parser[T], net_excepted: (String, T)*): Unit = {
    net_excepted.foreach(test_parser(parser))
  }

  test("NetlistParsers.Identifier") {
    test_parser(identifier)("m13.m141.n13", "m13.m141.n13")
    test_parser(indexed_identifier)("datai[12]", IndexedIdentifier("datai", 12))
  }

  test("NetlistParsers.Expression") {
    test_parser_for_each(expression,
      ("0", Number(0)),
      ("1", Number(1)),
      ("1'b0", Number(0)),
      ("1'b1", Number(1)),
      ("iden", SingleIdentifier("iden")),
      ("idx[3]", IndexedIdentifier("idx", 3)),
    )
    test_parser_for_each(lvalue,
      ("iden", SingleIdentifier("iden")),
      ("idx[3]", IndexedIdentifier("idx", 3)),
    )
  }

  test("NetlistParsers.BehavioralStatements") {
    test_parser_for_each(range,
      ("[0:20]", (20, 0)),
      ("[16:32]", (32, 16)),
    )
    test_parser_for_each(assignment,
      ("datao[3] = n35", (IndexedIdentifier("datao", 3), SingleIdentifier("n35"))),
      ("test_so = 0", (SingleIdentifier("test_so"), Number(0))),
      ("test_so = 1'b1", (SingleIdentifier("test_so"), Number(1))),
    )
  }

  test("NetlistParsers.ModuleInstantiations") {
    test_parser_for_each(namedPortConnection,
      (".port_ident(wire_ident)", ("port_ident", SingleIdentifier("wire_ident"))),
      (".D(n5[3])", ("D", IndexedIdentifier("n5", 3))),
    )
    test_parser_for_each(listOfModuleConnections,
      (".A(n1), .B(n2), .Z(n3)", List(("A", SingleIdentifier("n1")), ("B", SingleIdentifier("n2")), ("Z", SingleIdentifier("n3")))),
      (".D(n4), .DN(n5[3])", List(("D", SingleIdentifier("n4")), ("DN", IndexedIdentifier("n5", 3)))),
    )
    test_parser_for_each(nameOfInstance,
      ("U1", SingleIdentifier("U1")),
      ("U3 [12:2]", ArrayedIdentifier("U3", (2, 12))),
    )
    test_parser_for_each(moduleInstance,
      ("U1 (.A(x))", (SingleIdentifier("U1"), List(("A", SingleIdentifier("x"))))),
      ("U2 [3:0]()", (ArrayedIdentifier("U2", (0, 3)), List())),
    )
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
