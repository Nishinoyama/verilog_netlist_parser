package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.netlist.{Combinational, NetlistModule, SequentialModule, unit}
import jp.ac.nara_k.info.verilog_netlist.netlist.unit.{Assignment, Instance, PortConnection, Wire}

import scala.collection.immutable.TreeMap

class EjectedModule(sequentialModule: SequentialModule) extends NetlistModule with Combinational {

  private val ff_count = sequentialModule.ffInstances.size
  private val _pseudo_inputs = (0 until ff_count).map(i => Wire(f"__pseudo_input_$i%05d")).toSet
  private val _pseudo_outputs = (0 until ff_count).map(i => Wire(f"__pseudo_output_$i%05d")).toSet

  private def dFFConnectedWires(port: String) =
    sequentialModule.ffInstances.values.map(x => x.wireConnectedPort(port))

  private val _pseudo_inputs_assignments = _pseudo_inputs.zip(dFFConnectedWires("Q")).map {
    case (x, Some(y)) => unit.Assignment(y, x)
  }

  private val _pseudo_outputs_assignments = _pseudo_outputs.zip(dFFConnectedWires("D")).map {
    case (x, Some(y)) => unit.Assignment(x, y)
  }

  private val _pseudo_inputs_inverter_instances = {
    _pseudo_inputs.zip(dFFConnectedWires("QN")).collect {
      case (x, Some(y)) =>
        (s"${x.ident.toUpperCase}_INV", Instance("IVI", List(PortConnection("A", x), unit.PortConnection("Z", y))))
    }
  }

  private val _inputs: Set[Wire] = sequentialModule.inputs -- Wire.scanWires() ++ _pseudo_inputs
  private val _outputs: Set[Wire] = sequentialModule.outputs -- Wire.scanWires() ++ _pseudo_outputs
  private val _assignments: Set[Assignment] =
    sequentialModule.assignments.filterNot(x => x.left_wire.ident.equals("test_so")) ++
      _pseudo_inputs_assignments ++
      _pseudo_outputs_assignments
  private val _instances: TreeMap[String, Instance] =
    sequentialModule.combInstances.filterNot(x => x._2.port_connections.exists(y => Wire.scanWires().contains(y.wire))) ++
      _pseudo_inputs_inverter_instances

  override def name: String = sequentialModule.name

  override def inputs: Set[Wire] = _inputs

  override def outputs: Set[Wire] = _outputs

  override def wires: Set[Wire] = sequentialModule.wires

  override def assignments: Set[Assignment] = _assignments

  override def instances: TreeMap[String, Instance] = _instances

  def primaryInputs: Set[Wire] = sequentialModule.inputs

  def primaryOutputs: Set[Wire] = sequentialModule.outputs

  def pseudoInputs: Set[Wire] = _pseudo_inputs

  def pseudoOutputs: Set[Wire] = _pseudo_inputs
}
