package jp.ac.nara_k.info.verilog_netlist.parser

import jp.ac.nara_k.info.verilog_netlist.graph.SingleAssignmentOnlyAcyclicModuleGraph
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

  private def test_parser_forall[T](parser: Parser[T], net_excepted: (String, T)*): Boolean = {
    net_excepted.forall(test_parser(parser))
  }

  test("NetlistParsers.Identifier") {
    test_parser(identifier)("m13.m141.n13", "m13.m141.n13")
    test_parser(indexed_identifier)("datai[12]", IndexedIdentifier("datai", 12))
  }

  test("NetlistParsers.Expression") {
    test_parser_forall(expression,
      ("0", Number(0)),
      ("1", Number(1)),
      ("1'b0", Number(0)),
      ("1'b1", Number(1)),
      ("iden", SingleIdentifier("iden")),
      ("idx[3]", IndexedIdentifier("idx", 3)),
    )
    test_parser_forall(lvalue,
      ("iden", SingleIdentifier("iden")),
      ("idx[3]", IndexedIdentifier("idx", 3)),
    )
  }

  test("NetlistParsers.BehavioralStatements") {
    test_parser_forall(range,
      ("[0:20]", (20, 0)),
      ("[16:32]", (32, 16)),
    )
    test_parser_forall(assignment,
      ("datao[3] = n35", (IndexedIdentifier("datao", 3), SingleIdentifier("n35"))),
      ("test_so = 0", (SingleIdentifier("test_so"), Number(0))),
      ("test_so = 1'b1", (SingleIdentifier("test_so"), Number(1))),
    )
  }

  test("NetlistParsers.ModuleInstantiations") {
    test_parser_forall(namedPortConnection,
      (".port_ident(wire_ident)", ("port_ident", SingleIdentifier("wire_ident"))),
      (".D(n5[3])", ("D", IndexedIdentifier("n5", 3))),
    )
    test_parser_forall(listOfModuleConnections,
      (".A(n1), .B(n2), .Z(n3)", List(("A", SingleIdentifier("n1")), ("B", SingleIdentifier("n2")), ("Z", SingleIdentifier("n3")))),
      (".D(n4), .DN(n5[3])", List(("D", SingleIdentifier("n4")), ("DN", IndexedIdentifier("n5", 3)))),
    )
    test_parser_forall(nameOfInstance,
      ("U1", SingleIdentifier("U1")),
      ("U3 [12:2]", ArrayedIdentifier("U3", (2, 12))),
    )
    test_parser_forall(moduleInstance,
      ("U1 (.A(x))", (SingleIdentifier("U1"), List(("A", SingleIdentifier("x"))))),
      ("U2 [3:0]()", (ArrayedIdentifier("U2", (0, 3)), List())),
    )
    test_parser(moduleInstantiation)(
      (
        "IV1 U3 (.A(n3), .Z(n5));",
        ModuleInstance("IV1", SingleIdentifier("U3"), List(("A", SingleIdentifier("n3")), ("Z", SingleIdentifier("n5"))))
      )
    )
  }

  test("NetlistParsers.Declarations") {
    test_parser_forall(continuousAssign,
      ("assign x = y;", List(Assignment(SingleIdentifier("x"), SingleIdentifier("y")))),
      (
        "assign n1 = 1'b1, datao[0] = 1'b0;",
        List(Assignment(SingleIdentifier("n1"), Number(1)), Assignment(IndexedIdentifier("datao", 0), Number(0)))),
    )
    test_parser(wireDeclaration)("wire x, y, z;", List(SingleIdentifier("x"), SingleIdentifier("y"), SingleIdentifier("z")))
    test_parser(outputDeclaration)("output x, y;", List(SingleIdentifier("x"), SingleIdentifier("y")))
    test_parser(inputDeclaration)("input[0:20] x, y;", List(ArrayedIdentifier("x", (20, 0)), ArrayedIdentifier("y", (20, 0))))
    test_parser_forall(moduleItem,
      ("wire x;", List(WireDeclaration(SingleIdentifier("x")))),
      ("input x;", List(InputDeclaration(SingleIdentifier("x")))),
      ("output x;", List(OutputDeclaration(SingleIdentifier("x")))),
      ("assign x = y;", List(Assignment(SingleIdentifier("x"), SingleIdentifier("y")))),
      ("IV1 U(.A(n));", List(ModuleInstance("IV1", SingleIdentifier("U"), List(("A", SingleIdentifier("n")))))),
    )
    test_parser(module)(
      (
        "module b01(a, z); input a; output z; IV1 U1(.A(a), .Z(z)); endmodule",
        Module(name = "b01", ports = List("a", "z"), items = List(
          InputDeclaration(SingleIdentifier("a")),
          OutputDeclaration(SingleIdentifier("z")),
          ModuleInstance("IV1", SingleIdentifier("U1"), List(
            ("A", SingleIdentifier("a")),
            ("Z", SingleIdentifier("z")),
          ),
          )
        ))
      )
    )
  }

  test("NetlistParsers.parseISCAS") {
    val source = Source.fromFile("b17_net.v")
    val b02_net = source.getLines().mkString("\n")
    source.close()
    val parseResult = module(genTokenReader(b02_net)).get
    val analyzedModule = new AnalyzedSingleAssignmentOnlyModule(parseResult)
    val moduleGraph = new SingleAssignmentOnlyAcyclicModuleGraph(analyzedModule)
    //    println(moduleGraph)
    println(moduleGraph.search("datai__2"))
    println(moduleGraph.relation("P2_U1283", "P2_U895"))
  }

  test("NetlistParsers.parseMinimal") {
    val netlist =
      """
        |module tt(x, y, cx, z, cz);
        |  input [1:0] x;
        |  input [1:0] y;
        |  input cx;
        |  output [1:0] z;
        |  output cz;
        |  wire [1:0] n;
        |  wire nz;
        |  assign cz = n[0];
        |  ND2I P1 ( .A(x[0]), .B(y[0]), .Z(n[0]) );
        |  ND2I P2 ( .A(x[1]), .B(y[1]), .Z(n[1]) );
        |  ND2I P3 ( .A(n[0]), .B(cx), .Z(z[0]) );
        |  ND2I P4 ( .A(n[1]), .B(nz), .Z(z[1]) );
        |  FD2S P5 ( .D(n[0]), .Q(nz));
        |endmodule
        |""".stripMargin
    val parseResult = module(genTokenReader(netlist)).get
    val analyzedModule = new AnalyzedSingleAssignmentOnlyModule(parseResult)
    val moduleGraph = new SingleAssignmentOnlyAcyclicModuleGraph(analyzedModule)
    //    println(moduleGraph)
    assert(moduleGraph.search("x__0") == Set("P1", "P3", "x__0", "z__0", "n__0"))
    assert(moduleGraph.search("y__0") == Set("P1", "P3", "y__0", "z__0", "n__0"))
    assert(moduleGraph.search("x__1") == Set("P2", "P4", "x__1", "z__1", "n__1"))
    assert(moduleGraph.search("y__1") == Set("P2", "P4", "y__1", "z__1", "n__1"))
    assert(moduleGraph.search("cx") == Set("P3", "cx", "z__0"))
  }
}
