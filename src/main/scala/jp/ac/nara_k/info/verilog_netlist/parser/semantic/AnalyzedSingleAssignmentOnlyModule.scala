package jp.ac.nara_k.info.verilog_netlist.parser.semantic

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.ArrayedIndexedIntoSingleIdentifier._
import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst._

class AnalyzedSingleAssignmentOnlyModule(module: Module) {
  val inputs: Set[SingleIdentifier] = module.inputs.flatMap(convertSingles).toSet

  val outputs: Set[SingleIdentifier] = module.outputs.flatMap(convertSingles).toSet

  val wires: Set[SingleIdentifier] = module.wires.flatMap(convertSingles).toSet

  val assignments: Set[SingleAssignment] = module.assignments.map(convertAssignment).toSet

  val instantiated_modules: Set[ModuleInstance] = module.instances.map(convertInstance).toSet

  override def toString: String = s"inputs: $inputs\noutputs: $outputs\nwires: $wires\nassignments: $assignments\ninstantiatedModules: $instantiated_modules"
}
