package jp.ac.nara_k.info.verilog_netlist.parser.semantic

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst._

class AnalyzedSingleAssignmentOnlyModule(module: Module) {

  import ArrayedIndexedIntoSingleIdentifier._

  private val inputs = module.items.collect { case InputDeclaration(input) => convert(input).map(_.ident) }.flatten
  private val outputs = module.items.collect { case OutputDeclaration(output) => convert(output).map(_.ident) }.flatten
  private val wires = module.items.collect { case WireDeclaration(wire) => convert(wire).map(_.ident) }.flatten
  private val assignments = module.items.collect { case Assignment(lvalue, expression) =>
    (convertNonArrayed(lvalue).ident, convertExpression(expression))
  }
  private val instantiatedModules = module.items.collect { case ModuleInstance(module_name, declaration, port_connections) =>
    (module_name, convertNonArrayed(declaration).ident, port_connections.map { x => (x._1, convertExpression(x._2)) })
  }

  override def toString: String = {
    s"inputs: $inputs\noutputs: $outputs\nwires: $wires\nassignments: $assignments\ninstantiatedModules: $instantiatedModules"
  }
}
