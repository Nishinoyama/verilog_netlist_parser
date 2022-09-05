package jp.ac.nara_k.info.verilog_netlist.netlist

import jp.ac.nara_k.info.verilog_netlist.time_expansion.EjectedModule
import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class NetlistModuleTest extends AnyFunSuite {

  test("AnyModule.serializeDeserializeSerialize") {
    val source = Source.fromFile("b17_net.v")
    val b02_net = source.getLines().mkString("\n")
    source.close()
    val sequentialModule = AnyModule(b02_net)
    val serialized = sequentialModule.toString
    val serSequentialModule = AnyModule(serialized)
    assert(sequentialModule.toString.equals(serSequentialModule.toString))
  }

  test("SequentialModule.parseISCAS") {
    val source = Source.fromFile("b17_net.v")
    val b02_net = source.getLines().mkString("\n")
    source.close()
    val sequentialModule = SequentialModule(b02_net, List("FD2S"))
    val serialized = sequentialModule.toString
    val serSequentialModule = SequentialModule(serialized, List("FD2S"))
    assert(sequentialModule.toString.equals(serSequentialModule.toString))
  }

  test("SequentialModule.ejection") {
    val source = Source.fromFile("b17_net.v")
    val b02_net = source.getLines().mkString("\n")
    source.close()
    val sequentialModule = SequentialModule(b02_net, List("FD2S"))
    val ejectedModule = new EjectedModule(sequentialModule)
    val serialized = ejectedModule.toString
    println(serialized)
  }
}
