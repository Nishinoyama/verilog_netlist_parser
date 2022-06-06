package jp.ac.nara_k.info.verilog_netlist.parser.semantic

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst._

class AnalyzedSingleAssignmentOnlyModule(module: Module) extends AnalyzedModule {

  import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst.ArrayedIndexedIntoSingleIdentifier._

  private val _inputs = module.items.collect { case InputDeclaration(input) => convert(input).map(_.ident) }.flatten.toSet
  private val _outputs = module.items.collect { case OutputDeclaration(output) => convert(output).map(_.ident) }.flatten.toSet
  private val _wires = module.items.collect { case WireDeclaration(wire) => convert(wire).map(_.ident) }.flatten.toSet
  private val _assignments = module.items.collect { case Assignment(lvalue, expression) =>
    (convertNonArrayed(lvalue).ident, convertExpression(expression))
  }.toSet
  private val instantiated_modules = module.items.collect { case ModuleInstance(module_name, declaration, port_connections) =>
    (convertNonArrayed(declaration).ident, (module_name, port_connections.map { x => (x._1, convertExpression(x._2)) }))
  }.toMap

  override def toString: String = s"inputs: $inputs\noutputs: $outputs\nwires: $wires\nassignments: $assignments\ninstantiatedModules: $instantiatedModules"

  override def inputs: Set[String] = _inputs

  override def outputs: Set[String] = _outputs

  override def wires: Set[String] = _wires

  override def assignments: Set[(String, Expression)] = _assignments

  override def instantiatedModules: Map[String, (String, Iterable[(String, Expression)])] = instantiated_modules
}
