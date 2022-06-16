package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.input.NetlistTokenReader
import jp.ac.nara_k.info.verilog_netlist.parser.lexical.StdNetlistLexical
import jp.ac.nara_k.info.verilog_netlist.parser.semantic.AnalyzedSingleAssignmentOnlyModule
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class SequentialModuleTest extends AnyFunSuite {

  object netlist_parsers extends jp.ac.nara_k.info.verilog_netlist.parser.NetlistParsers

  import netlist_parsers._

  private def lex(net: String) = {
    object lexical extends StdNetlistLexical
    import lexical._
    parse(tokens, net).getOrElse(List.empty)
  }

  private def genTokenReader(net: String) = {
    new NetlistTokenReader(lex(net))
  }

  test("SequentialModule.parseISCAS") {
    val source = Source.fromFile("b17_net.v")
    val b02_net = source.getLines().mkString("\n")
    source.close()
    val parseResult = module(genTokenReader(b02_net)).get
    val analyzedModule = new AnalyzedSingleAssignmentOnlyModule(parseResult)
    val sequentialModule = new SequentialModule(analyzedModule, List("FD2S"))
    val serialized = sequentialModule.toString
    val serParseResult = module(genTokenReader(serialized)).get
    val serAnalyzedModule = new AnalyzedSingleAssignmentOnlyModule(serParseResult)
    val serSequentialModule = new SequentialModule(serAnalyzedModule, List("FD2S"))
    assert(sequentialModule.toString.equals(serSequentialModule.toString))
  }
}
