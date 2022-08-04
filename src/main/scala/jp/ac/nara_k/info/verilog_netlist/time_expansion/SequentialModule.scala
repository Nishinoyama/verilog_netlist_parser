package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.time_expansion.unit.{Assignment, Instance, Wire}

import scala.collection.immutable.TreeMap

class SequentialModule(module: AnyModule, val ff_names: Iterable[String])
  extends NetlistModule with Sequential {

  override def ffInstances: TreeMap[String, Instance] = _ff_instances

  override def combInstances: TreeMap[String, Instance] = _comb_instances

  override def name: String = module.name

  override def inputs: Set[Wire] = module.inputs

  override def outputs: Set[Wire] = module.outputs

  override def wires: Set[Wire] = module.wires

  override def assignments: Set[Assignment] = module.assignments

  override def instances: Map[String, Instance] = module.instances

  private val (_ff_instances, _comb_instances) = TreeMap.from(instances).partition(module => ff_names.exists(module._2.name.equals))
}

object SequentialModule {
  def apply(netlist_serial: String, ff_names: Iterable[String]): SequentialModule = {
    new SequentialModule(AnyModule(netlist_serial), ff_names)
  }
}
