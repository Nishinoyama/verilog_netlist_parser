package jp.ac.nara_k.info.verilog_netlist.time_expansion

import scala.collection.immutable.TreeMap

class EjectedModule(sequentialModule: SequentialModule) extends Module {

  private val ff_count = sequentialModule.ff_instances.size
  private val _pseudo_inputs = (0 until ff_count).map(i => new Wire(f"__pseudo_input_$i%05d")).toSet
  private val _pseudo_outputs = (0 until ff_count).map(i => new Wire(f"__pseudo_output_$i%05d")).toSet

  private val _pseudo_inputs_assignments = {
    _pseudo_inputs.zip(dFFConnectedWires("Q")).map {
      case (x, Some(y)) => new Assignment(x, y)
    }
  }

  private val _pseudo_outputs_assignments = {
    _pseudo_outputs.zip(dFFConnectedWires("D")).map {
      case (x, Some(y)) => new Assignment(y, x)
    }
  }

  private def dFFConnectedWires(port: String) =
    sequentialModule.ff_instances.map(x => x._2.wireConnectedPort(port))

  private val _pseudo_inputs_inverter_instances = {
    _pseudo_inputs.zip(dFFConnectedWires("QN")).collect {
      case (x, Some(y)) => (s"${x.ident.toUpperCase}_INV", new Instance("IV", List(
        new PortConnection("A", x), new PortConnection("Z", y)
      )))
    }
  }

  override def name: String = sequentialModule.name

  override def inputs: Set[Wire] = sequentialModule.inputs ++ _pseudo_inputs

  override def outputs: Set[Wire] = sequentialModule.outputs ++ _pseudo_outputs

  override def wires: Set[Wire] = sequentialModule.wires

  override def assignments: Set[Assignment] = sequentialModule.assignments ++ _pseudo_inputs_assignments ++ _pseudo_outputs_assignments

  override def instances: TreeMap[String, Instance] = sequentialModule.instances ++ _pseudo_inputs_inverter_instances

  def primaryInputs: Set[Wire] = sequentialModule.inputs

  def primaryOutputs: Set[Wire] = sequentialModule.outputs

  def pseudoInputs: Set[Wire] = _pseudo_inputs

  def pseudoOutputs: Set[Wire] = _pseudo_inputs
}
