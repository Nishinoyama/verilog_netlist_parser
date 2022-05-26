package jp.ac.nara_k.info.verilog_netlist.parser.ast

object NetlistAst {

  trait ModuleItem

  trait Identifier {
    val ident: String
  }

  trait Declaration extends Identifier

  trait Arrayed {
    val range: (Int, Int)
  }

  trait Expression

  case class Module(name: String, ports: Seq[String], items: Seq[ModuleItem])

  case class InputDeclaration(inputs: Declaration) extends ModuleItem

  case class OutputDeclaration(outputs: Declaration) extends ModuleItem

  case class WireDeclaration(wires: Declaration) extends ModuleItem

  case class Assignment(lvalue: Identifier, expression: Expression) extends ModuleItem

  //  case class ModulesDeclaration(modules: Seq[ModuleInstance]) extends ModuleItem

  case class ModuleInstance(module_name: String, declaration: Declaration, port_connections: Seq[(String, Expression)]) extends ModuleItem

  case class SingleIdentifier(ident: String) extends Declaration with Expression

  case class ArrayedIdentifier(ident: String, range: (Int, Int)) extends Declaration with Arrayed

  case class IndexedIdentifier(ident: String, index: Int) extends Identifier with Expression

  case class Number(value: Int) extends Expression
}
