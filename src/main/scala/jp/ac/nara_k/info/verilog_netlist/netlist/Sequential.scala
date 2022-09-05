package jp.ac.nara_k.info.verilog_netlist.netlist

import jp.ac.nara_k.info.verilog_netlist.netlist.unit.Instance

import scala.collection.immutable.TreeMap

trait Sequential {
  def ffInstances: TreeMap[String, Instance]

  def combInstances: TreeMap[String, Instance]
}
