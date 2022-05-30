package jp.ac.nara_k.info.verilog_netlist.parser

import jp.ac.nara_k.info.verilog_netlist.graph.SingleAssignmentOnlyModuleGraph
import jp.ac.nara_k.info.verilog_netlist.parser.input.NetlistTokenReader
import jp.ac.nara_k.info.verilog_netlist.parser.lexical.StdNetlistLexical
import jp.ac.nara_k.info.verilog_netlist.parser.semantic.AnalyzedSingleAssignmentOnlyModule
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class NetlistParsersTest extends AnyFunSuite {

  test("NetlistParsers.parseISCAS_b02") {
    val source = Source.fromFile("b17_net.v")
    val b02_net = source.getLines().mkString("\n")
    source.close()
    object lexical extends StdNetlistLexical
    import jp.ac.nara_k.info.verilog_netlist.parser.token.NetlistTokens.ErrorToken
    import lexical._
    val tokenList = parse(tokens, b02_net)
    assert(!tokenList.get.exists(_.isInstanceOf[ErrorToken]))
    object parser extends NetlistParsers
    val reader = new NetlistTokenReader(tokenList.get)
    val parseResult = parser.module(reader)
    val analyzedModule = new AnalyzedSingleAssignmentOnlyModule(parseResult.get)
    val moduleGraph = new SingleAssignmentOnlyModuleGraph(analyzedModule)
//    println(moduleGraph)
    println(moduleGraph.search("datai__2"))
    println(moduleGraph.relation("P2_U1283", "P2_U895"))
  }
}
