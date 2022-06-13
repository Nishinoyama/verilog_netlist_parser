package jp.ac.nara_k.info.verilog_netlist.graph

import jp.ac.nara_k.info.verilog_netlist.parser.ast.NetlistAst._
import jp.ac.nara_k.info.verilog_netlist.parser.semantic.AnalyzedSingleAssignmentOnlyModule

import scala.collection.mutable

class SingleAssignmentOnlyAcyclicModuleGraph(module: AnalyzedSingleAssignmentOnlyModule) extends NetlistGraph[String] {

  override val output_port_names: mutable.HashSet[String] = mutable.HashSet.from(List("Z"))
  override val ff_names: mutable.HashSet[String] = mutable.HashSet.from(List("FD2S"))
  override val nodes: mutable.HashSet[String] = mutable.HashSet.empty

  nodes ++= module.inputs.map(_.ident)
  nodes ++= module.outputs.map(_.ident)
  nodes ++= module.wires.map(_.ident)
  nodes ++= module.instantiated_modules.map(_.declaration.ident)

  override val edges: mutable.HashMap[String, mutable.HashSet[String]] = mutable.HashMap.from(nodes.map((_, mutable.HashSet.empty)))

  module.instantiated_modules.foreach {
    case ModuleInstance(module_name, SingleIdentifier(module_ident), ports_connect) =>
      if (!ff_names.contains(module_name)) {
        ports_connect.foreach {
          case (port, SingleIdentifier(wire_ident)) =>
            if (output_port_names.contains(port))
              edges(module_ident).add(wire_ident)
            else
              edges(wire_ident).add(module_ident)
          case _ => ()
        }
      }
  }
  module.assignments.foreach {
    case SingleAssignment(SingleIdentifier(lvalue), SingleIdentifier(ident)) => edges(lvalue) += ident
    case _ => ()
  }

  override def toString: String = {
    s"$nodes\n${edges.filter(x => x._2.nonEmpty)}"
  }

  def search(root: String): mutable.HashSet[String] = {
    val stack = mutable.Stack.empty[String]
    val searched = mutable.HashSet.empty[String]
    stack += root
    searched += root
    while (stack.nonEmpty) {
      val from = stack.pop()
      edges(from).diff(searched).foreach { to =>
        stack += to
        searched += to
      }
    }
    searched
  }

  /** *
   *
   * @param l String of module identity
   * @param r String of module identity
   * @return 1 if the subgraph of l contains r, -1 if the subgraph of r contains l,
   *         2 if the two subgraph have NO common nodes, 0 otherwise
   */
  def relation(l: String, r: String): Int = {
    val l_sub = search(l)
    if (l_sub.contains(r)) 1
    else {
      val r_sub = search(r)
      if (r_sub.contains(l)) -1
      else if ((l_sub & r_sub).isEmpty) 2
      else 0
    }
  }
}
