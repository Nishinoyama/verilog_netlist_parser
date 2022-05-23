package jp.ac.nara_k.info.verilog_netlist

import jp.ac.nara_k.info.verilog_netlist.module._
import org.scalatest.funsuite.AnyFunSuite

class TopModuleTest extends AnyFunSuite {
  test("netlist") {
    object TopModuleObject extends StdTopModule("test_net")
    val a = new InputWire("a")
    val b = new InputWire("b")
    val c = new MultiInputWire("c", (0, 7))
    val x = new OutputWire("x")
    val y = new OutputWire("y")
    val z = new MultiOutputWire("z", (0, 3))
    val n1 = new InnerWire("n1")
    val n2 = new InnerWire("n2")
    val stato = new MultiWire("stato", (0, 1))
    TopModuleObject.inputs.addAll(List(a, b, c))
    TopModuleObject.outputs.addAll(List(x, y, z))
    TopModuleObject.wires.addAll(List(n1, n2, stato))
    TopModuleObject.assigns.addAll(List(
      new Assignment(a, b),
      new Assignment(y, b),
    ))
    println(TopModuleObject.toString)
  }
}
