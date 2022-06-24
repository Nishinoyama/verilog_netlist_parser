package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.semantic.AnalyzedSingleAssignmentOnlyModule
import jp.ac.nara_k.info.verilog_netlist.time_expansion.unit.Instance

import scala.collection.immutable.TreeMap

class SequentialModule(singleAssignmentOnlyModule: AnalyzedSingleAssignmentOnlyModule, val ff_names: Iterable[String])
  extends AnyModule(singleAssignmentOnlyModule) {
  private val (_ff_instances, _comb_instances) = instances.partition(module => ff_names.exists(module._2.name.equals))

  def ffInstances: TreeMap[String, Instance] = _ff_instances

  def combInstances: TreeMap[String, Instance] = _comb_instances
}
