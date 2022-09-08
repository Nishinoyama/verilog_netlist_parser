package jp.ac.nara_k.info.verilog_netlist.netlist

import jp.ac.nara_k.info.verilog_netlist.netlist.unit.{Assignment, Instance, Wire}
import jp.ac.nara_k.info.verilog_netlist.parser.semantic.SinglizedModule

import scala.collection.immutable.TreeMap

class Module(singlizedModule: SinglizedModule) extends NetlistModule {
  override def name: String = singlizedModule.name

  override def inputs: Set[Wire] = singlizedModule.inputs.map(Wire(_))

  override def outputs: Set[Wire] = singlizedModule.outputs.map(Wire(_))

  override def wires: Set[Wire] = singlizedModule.wires.map(Wire(_))

  override def assignments: Set[Assignment] = singlizedModule.assignments.map(Assignment(_))

  override def instances: TreeMap[String, Instance] = TreeMap.from(
    singlizedModule.instantiated_modules.map(Instance.fromAst)
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

object Module {
  def apply(netlist_serial: String): Module = new Module(SinglizedModule(netlist_serial))

  def apply(singlizedModule: SinglizedModule): Module = new Module(singlizedModule)
}
