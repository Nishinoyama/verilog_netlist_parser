package jp.ac.nara_k.info.verilog_netlist.time_expansion.fault

import org.scalatest.funsuite.AnyFunSuite

class FaultTest extends AnyFunSuite {
  test("FaultTest.Fault") {
    val f = Fault("stf   --   U276/C")
    assert(f.alpha == 1)
    assert(!f.isStr)
    assert(!f.location.isTop)
    assert(f.toString == "stf -- U276/C")

    val g = Fault("str   DI   test_si")
    assert(g.alpha == 0)
    assert(g.isStr)
    assert(g.location.isTop)
    assert(g.toString == "str DI test_si")
  }

  test("FaultTest.Location") {
    val f = Location("U314/Z").asInstanceOf[LocationInstance]
    assert(!f.isTop)
    assert(f.instanceIdent == "U314")
    assert(f.pin == "Z")
    assert(f.toString == "U314/Z")

    val g = Location("DATA__1").asInstanceOf[LocationTop]
    assert(g.isTop)
    assert(g.pin == "DATA__1")
    assert(g.toString == "DATA__1")
  }

  test("FaultTest.SlowTo") {
    val str = SlowTo.Rise
    assert(str.alpha == 0)
    assert(str.toString == "str")

    val stf = SlowTo.Fall
    assert(stf.alpha == 1)
    assert(stf.toString == "stf")
  }
}
