package jp.ac.nara_k.info.verilog_netlist.time_expansion

trait Module {
  def name: String

  def inputs: Set[Wire]

  def outputs: Set[Wire]

  def wires: Set[Wire]

  def assignments: Set[Assignment]

  def instances: Map[String, Instance]

  override def toString: String = {
    s"""module $name (${(inputs ++ outputs).toSeq.sorted.mkString(", ")});
       |  input ${inputs.toSeq.sorted.mkString(", ")};
       |  output ${outputs.toSeq.sorted.mkString(", ")};
       |  wire ${wires.toSeq.sorted.mkString(", ")};
       |  ${instances.map(x => x._2.serialized(x._1)).toSeq.sorted.mkString(";\n  ")};
       |endmodule
       |""".stripMargin
  }
}
