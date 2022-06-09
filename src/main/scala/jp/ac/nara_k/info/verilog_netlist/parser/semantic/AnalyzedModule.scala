package jp.ac.nara_k.info.verilog_netlist.parser.semantic

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst._

class AnalyzedModule(module: Module) {
  val inputs: Set[InputDeclaration] = module.items.collect {
    case input: InputDeclaration => input
  }.toSet

  val outputs: Set[OutputDeclaration] = module.items.collect {
    case output: OutputDeclaration => output
  }.toSet

  val wires: Set[WireDeclaration] = module.items.collect {
    case wire: WireDeclaration => wire
  }.toSet

  val assignments: Set[Assignment] = module.items.collect {
    case assign: Assignment => assign
  }.toSet

  val instantiated_modules: Map[Declaration, ModuleInstance] = module.items.collect {
    case instance: ModuleInstance => (instance.declaration, instance)
  }.toMap
}
