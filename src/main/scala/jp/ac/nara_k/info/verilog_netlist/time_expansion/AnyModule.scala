package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.parser.semantic.AnalyzedSingleAssignmentOnlyModule
import jp.ac.nara_k.info.verilog_netlist.time_expansion.unit.{Assignment, Instance, Wire}

import scala.collection.immutable.TreeMap

class AnyModule(singleAssignmentOnlyModule: AnalyzedSingleAssignmentOnlyModule) extends NetlistModule {
  override def name: String = singleAssignmentOnlyModule.name

  override def inputs: Set[Wire] = singleAssignmentOnlyModule.inputs.map(Wire(_))

  override def outputs: Set[Wire] = singleAssignmentOnlyModule.outputs.map(Wire(_))

  override def wires: Set[Wire] = singleAssignmentOnlyModule.wires.map(Wire(_))

  override def assignments: Set[Assignment] = singleAssignmentOnlyModule.assignments.map(Assignment(_))

  override def instances: TreeMap[String, Instance] = TreeMap.from(
    singleAssignmentOnlyModule.instantiated_modules.map(Instance.fromAst)
  )

  override def toString: String = {
    s"""module $name (${(inputs ++ outputs).toSeq.sorted.mkString(", ")});
       |  input ${inputs.toSeq.sorted.mkString(", ")};
       |  output ${outputs.toSeq.sorted.mkString(", ")};
       |  wire ${wires.toSeq.sorted.mkString(", ")};
       |  ${instances.map(x => x._2.serialized(x._1)).toSeq.mkString(";\n  ")};
       |endmodule
       |""".stripMargin
  }
}
