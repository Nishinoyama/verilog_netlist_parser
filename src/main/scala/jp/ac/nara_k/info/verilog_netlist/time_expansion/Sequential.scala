package jp.ac.nara_k.info.verilog_netlist.time_expansion

import jp.ac.nara_k.info.verilog_netlist.time_expansion.unit.Instance

import scala.collection.immutable.TreeMap

trait Sequential {
  def ffInstances: TreeMap[String, Instance]

  def combInstances: TreeMap[String, Instance]
}
