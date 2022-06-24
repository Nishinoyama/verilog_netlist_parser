package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.time_expansion.unit.{Assignment, Instance, Wire}

abstract class NetlistModule {
  def name: String

  def inputs: Set[Wire]

  def outputs: Set[Wire]

  def wires: Set[Wire]

  def assignments: Set[Assignment]

  def instances: Map[String, Instance]

  def toInstance: Instance = Instance.fromNetlistModule(this)

  override def toString: String = {
    s"""module $name (${(inputs ++ outputs).toSeq.sorted.mkString(", ")});
       |  input ${inputs.toSeq.sorted.mkString(", ")};
       |  output ${outputs.toSeq.sorted.mkString(", ")};
       |  wire ${wires.toSeq.sorted.mkString(", ")};
       |  ${assignments.map(_.toString).mkString(";\n  ")};
       |  ${instances.map(x => x._2.serialized(x._1)).toSeq.sorted.mkString(";\n  ")};
       |endmodule
       |""".stripMargin
  }
}
