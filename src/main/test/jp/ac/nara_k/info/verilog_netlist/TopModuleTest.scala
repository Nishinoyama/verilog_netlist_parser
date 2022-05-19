package jp.ac.nara_k.info.verilog_netlist

import jp.ac.nara_k.info.verilog_netlist.module._
import org.scalatest.funsuite.AnyFunSuite

class TopModuleTest extends AnyFunSuite {
  test("netlist") {
    object TopModuleObject extends StdTopModule("test_net")
    class StdInput(override val ident: String) extends Input
    class StdMultiInput(override val ident: String, override val range: (Int, Int)) extends MultiInput
    class StdOutput(override val ident: String) extends Output
    class StdMultiOutput(override val ident: String, override val range: (Int, Int)) extends MultiOutput
    class StdWire(override val ident: String) extends Wire
    class StdMultiWire(override val ident: String, override val range: (Int, Int)) extends MultiWire
    class StdAssignment(lvalue_str: String, rvalue_str: String) extends Assignment {
      class AssignIdentifier(override val ident: String) extends Identifier

      override val lvalue = new AssignIdentifier(lvalue_str)
      override val rvalue = new AssignIdentifier(rvalue_str)
    }
    TopModuleObject.inputs.addAll(List(new StdInput("a"), new StdInput("b"), new StdMultiInput("c", (0, 7))))
    TopModuleObject.outputs.addAll(List(new StdOutput("x"), new StdOutput("y"), new StdMultiOutput("z", (0, 3))))
    TopModuleObject.wires.addAll(List(new StdWire("n1"), new StdWire("n2"), new StdMultiWire("stato", (0, 1))))
    TopModuleObject.assigns.addAll(List(
      new StdAssignment("x", "a"),
      new StdAssignment("y", "a"),
    ))
    println(TopModuleObject.toString)
  }
}
