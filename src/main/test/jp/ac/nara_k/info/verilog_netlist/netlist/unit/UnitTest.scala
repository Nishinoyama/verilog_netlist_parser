package jp.ac.nara_k.info.verilog_netlist.netlist.unit

import org.scalatest.funsuite.AnyFunSuite

class UnitTest extends AnyFunSuite {
  test("Unit.Wire") {
    val w = Wire("n11")
    val s = Wire("n11")
    val t = Wire("n9")
    val u = ConstWire(0)
    val v = ConstWire(1)
    val x = UnusedWire
    assert(w.ident == "n11")
    assert(w == s)
    assert(w.hashCode == s.hashCode)
    assert(w != t)
    assert(!w.isConst)
    assert(u.isConst)
    assert(u.constVal == 0)
    assert(v.isConst)
    assert(v.constVal == 1)
    assert(!x.isConst)
    try {
      x.toString
      fail("Unused wire can be string")
    } catch {
      case _: Exception => ()
    }
  }
  test("Unit.PortConnection") {
    val p = PortConnection("A", Wire("n13"))
    assert(p.port == "A")
    assert(p.wire == Wire("n13"))
    val q = PortConnection("Z")
    assert(q.port == "Z")
    assert(!q.wire.isValid)
    val r = PortConnection.fromWire(Wire("DATA_IN__3"))
    assert(r.port == "DATA_IN__3")
    assert(!r.wire.isValid)
    val sr = r.wireConnected(Wire("x"))
    assert(sr.port == "DATA_IN__3")
    assert(sr.wire == Wire("x"))
  }
}
