package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.semantic.AnalyzedSingleAssignmentOnlyModule

class SequentialModule(singleAssignmentOnlyModule: AnalyzedSingleAssignmentOnlyModule, val ff_names: Iterable[String]) {
  private val _name = singleAssignmentOnlyModule.name
  private val _inputs = singleAssignmentOnlyModule.inputs.map(Wire.fromAst)
  private val _outputs = singleAssignmentOnlyModule.outputs.map(Wire.fromAst)
  private val _wires = singleAssignmentOnlyModule.wires.map(Wire.fromAst)
  private val _assignments = singleAssignmentOnlyModule.assignments.map(Assignment.fromAst)
  private val (_ff_instances, _instances) = singleAssignmentOnlyModule.instantiated_modules
    .map(Instance.fromAst)
    .toMap
    .partition(module => ff_names.exists(module._2.name.equals))

  def name: String = _name

  def inputs: Set[Wire] = _inputs

  def outputs: Set[Wire] = _outputs

  def wires: Set[Wire] = _wires

  def assignments: Set[Assignment] = _assignments

  def instances: Map[String, Instance] = _instances

  def ff_instances: Map[String, Instance] = _ff_instances

  override def toString: String = {
    s"""module $name (${(inputs ++ outputs).toSeq.sorted.mkString(", ")});
       |  input ${inputs.toSeq.sorted.mkString(", ")};
       |  output ${outputs.toSeq.sorted.mkString(", ")};
       |  wire ${wires.toSeq.sorted.mkString(", ")};
       |  ${instances.map(x => x._2.serialized(x._1)).toSeq.sorted.mkString(";\n  ")};
       |
       |  ${ff_instances.map(x => x._2.serialized(x._1)).toSeq.sorted.mkString(";\n  ")};
       |endmodule
       |""".stripMargin
  }
}
