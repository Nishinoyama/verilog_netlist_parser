package jp.ac.nara_k.info.verilog_netlist.parser.semantic

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.ArrayedIndexedIntoSingleIdentifier._
import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst._

class SinglizedModule(module: Module) extends AnalyzedModule[SingleIdentifier, SingleAssignment, ModuleInstance] {
  val name: String = module.name

  override val inputs: Set[SingleIdentifier] = module.inputs.flatMap(convertSingles).toSet

  override val outputs: Set[SingleIdentifier] = module.outputs.flatMap(convertSingles).toSet

  override val wires: Set[SingleIdentifier] = module.wires.flatMap(convertSingles).toSet

  override val assignments: Set[SingleAssignment] = module.assignments.flatMap(convertAssignment).toSet

  override val instantiated_modules: Set[ModuleInstance] = module.instances.flatMap(convertInstance).toSet

  override def toString: String = s"inputs: $inputs\noutputs: $outputs\nwires: $wires\nassignments: $assignments\ninstantiatedModules: $instantiated_modules"
}

object SinglizedModule {

  import jp.ac.nara_k.info.verilog_netlist.parser.NetlistParsers
  import jp.ac.nara_k.info.verilog_netlist.parser.input.NetlistTokenReader
  import jp.ac.nara_k.info.verilog_netlist.parser.lexical.StdNetlistLexical

  object netlist_parsers extends NetlistParsers
  import netlist_parsers._

  private def lex(net: String) = {
    object lexical extends StdNetlistLexical
    import lexical._
    parse(tokens, net).getOrElse(List.empty)
  }

  private def genTokenReader(net: String) = {
    new NetlistTokenReader(lex(net))
  }

  def apply(netlist_serial: String): SinglizedModule = {
    val parseResult = module(genTokenReader(netlist_serial)).get
    new SinglizedModule(parseResult)
  }

}