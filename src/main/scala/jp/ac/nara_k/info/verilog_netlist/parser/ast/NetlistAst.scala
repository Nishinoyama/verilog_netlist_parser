package jp.ac.nara_k.info.verilog_netlist.parser.ast

object NetlistAst {

  trait ModuleItem

  trait Identifier {
    val ident: String
  }

  trait Declaration

  trait Arrayed {
    val range: (Int, Int)
  }

  trait Expression

  case class Module(name: String, ports: Seq[String], item: Seq[ModuleItem])

  case class InputsDeclaration(inputs: Seq[Declaration]) extends ModuleItem

  case class OutputsDeclaration(outputs: Seq[Declaration]) extends ModuleItem

  case class WiresDeclaration(wires: Seq[Declaration]) extends ModuleItem

  case class ContinuousAssignments(assignments: Seq[Assignment]) extends ModuleItem

  case class Assignment(lvalue: Identifier, expression: Expression)

  case class ModulesDeclaration(modules: Seq[ModuleInstance]) extends ModuleItem

  case class ModuleInstance(module_name: String, declaration: Declaration, port_connections: Seq[(String, Expression)]) extends ModuleItem

  case class SingleIdentifier(ident: String) extends Identifier with Expression with Declaration

  case class ArrayedIdentifier(ident: String, range: (Int, Int)) extends Identifier with Arrayed with Declaration

  case class IndexedIdentifier(ident: String, index: Int) extends Identifier with Expression

  case class Number(value: Int) extends Expression
}
