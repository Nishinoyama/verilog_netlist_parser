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

  trait Assignment extends ModuleItem {
    val lvalue: Identifier
    val expression: Expression
  }

  case class Module(name: String, ports: Seq[String], items: Seq[ModuleItem]) {
    def inputs: Iterable[Declaration] = items.collect {
      case InputDeclaration(input) => input
    }

    def outputs: Iterable[Declaration] = items.collect {
      case OutputDeclaration(output) => output
    }

    def wires: Iterable[Declaration] = items.collect {
      case WireDeclaration(wire) => wire
    }

    def assignments: Iterable[Assignment] = items.collect {
      case assignment: Assignment => assignment
    }

    def instances: Iterable[ModuleInstance] = items.collect {
      case moduleInstance: ModuleInstance => moduleInstance
    }
  }

  case class InputDeclaration(input: Declaration) extends ModuleItem

  case class OutputDeclaration(output: Declaration) extends ModuleItem

  case class WireDeclaration(wire: Declaration) extends ModuleItem

  case class SingleAssignment(lvalue: SingleIdentifier, expression: Expression) extends Assignment

  case class ArrayedAssignment(lvalue: ArrayedIdentifier, expression: Expression) extends Assignment

  case class IndexedAssignment(lvalue: IndexedIdentifier, expression: Expression) extends Assignment

  //  case class ModulesDeclaration(modules: Seq[ModuleInstance]) extends ModuleItem

  case class ModuleInstance(module_name: String, declaration: Declaration, port_connections: Seq[(String, Expression)]) extends ModuleItem

  case class SingleIdentifier(ident: String) extends Declaration with Expression

  case class ArrayedIdentifier(ident: String, range: (Int, Int)) extends Declaration with Arrayed

  case class IndexedIdentifier(ident: String, index: Int) extends Identifier with Expression

  case class Number(value: Int) extends Expression

  object Assignment {
    def apply(lvalue: Identifier, expression: Expression): Assignment = lvalue match {
      case lvalue: SingleIdentifier => NetlistAst.SingleAssignment(lvalue, expression)
      case lvalue: IndexedIdentifier => NetlistAst.IndexedAssignment(lvalue, expression)
      case lvalue: ArrayedIdentifier => NetlistAst.ArrayedAssignment(lvalue, expression)
    }
  }


  object ArrayedIndexedIntoSingleIdentifier {

    private def convertedSingle(ident: String, index: Int): SingleIdentifier = {
      SingleIdentifier(ident + s"__$index")
    }

    def convertSingles(identifier: Identifier): Iterable[SingleIdentifier] = identifier match {
      case ArrayedIdentifier(ident, range) => (range._1 to range._2).map(convertedSingle(ident, _))
      case IndexedIdentifier(ident, index) => List(convertedSingle(ident, index))
      case ident: SingleIdentifier => List(ident)
    }

    def convertNonArrayed(identifier: Identifier): SingleIdentifier = identifier match {
      case IndexedIdentifier(ident, index) => convertedSingle(ident, index)
      case ident: SingleIdentifier => ident
    }

    def convertExpression(expression: Expression): Expression = expression match {
      case IndexedIdentifier(ident, index) => convertedSingle(ident, index)
      case x => x
    }

    def convertAll(identifiers: Iterable[Identifier]): Iterable[SingleIdentifier] = {
      identifiers flatMap convertSingles
    }

    def convertAssignment(assignment: Assignment): Iterable[SingleAssignment] = assignment match {
      case ArrayedAssignment(lvalue, expression) => ???
      case assigment => List(SingleAssignment(convertNonArrayed(assigment.lvalue), convertExpression(assignment.expression)))
    }

    def convertInstance(instance: ModuleInstance): Iterable[ModuleInstance] = instance match {
      case ModuleInstance(name, identifier: Identifier, port_connections) =>
        List(ModuleInstance(name, convertNonArrayed(identifier), port_connections.map(convertPortConnection)))
    }

    def convertPortConnection(port_connection: (String, Expression)): (String, Expression) = port_connection match {
      case (port, connection: Expression) => (port, convertExpression(connection))
    }

  }

}
