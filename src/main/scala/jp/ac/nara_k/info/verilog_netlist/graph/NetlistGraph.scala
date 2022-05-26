package jp.ac.nara_k.info.verilog_netlist.graph

import scala.collection.mutable

trait NetlistGraph[Node] {
  val nodes: mutable.HashSet[Node]
  val edges: mutable.HashMap[Node, mutable.HashSet[Node]]
  val output_port_names: mutable.HashSet[String]
  val ff_names: mutable.HashSet[String]

  def node_size: Int = nodes.size
}
