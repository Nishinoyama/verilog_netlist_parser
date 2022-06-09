package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.semantic.AnalyzedSingleAssignmentOnlyModule
import jp.ac.nara_k.info.verilog_netlist.parser.semantic.Assignment.Assignment
import jp.ac.nara_k.info.verilog_netlist.parser.semantic.PortWire.PortWire

class SequentialModule(singleAssignmentOnlyModule: AnalyzedSingleAssignmentOnlyModule, val ff_names: Iterable[String]) {
  private val _inputs = singleAssignmentOnlyModule.inputs
  private val _outputs = singleAssignmentOnlyModule.outputs
  private val _wires = singleAssignmentOnlyModule.wires
  private val _assignments = singleAssignmentOnlyModule.assignments
  private val (_ff_instances, _instances) = singleAssignmentOnlyModule.instantiated_modules.partition(i => ff_names.exists(i._2._1.equals))

  def inputs: Set[String] = _inputs

  def outputs: Set[String] = _outputs

  def wires: Set[String] = _wires

  def assignments: Set[Assignment] = _assignments

  def instances: Map[String, (String, Seq[PortWire])] = _instances

  def ff_instances: Map[String, (String, Seq[PortWire])] = _ff_instances
}
