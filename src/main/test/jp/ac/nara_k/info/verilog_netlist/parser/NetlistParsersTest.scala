package jp.ac.nara_k.info.verilog_netlist.parser

import jp.ac.nara_k.info.verilog_netlist.parser.input.NetlistTokenReader
import jp.ac.nara_k.info.verilog_netlist.parser.lexical.StdNetlistLexical
import org.scalatest.funsuite.AnyFunSuite

class NetlistParsersTest extends AnyFunSuite {

  test("StdNetlistLexical.parseISCAS_b02") {
    val b02_net =
      """
/////////////////////////////////////////////////////////////
// Created by: Synopsys DC Expert(TM) in wire load mode
// Version   : K-2015.06-SP1
// Date      : Tue Apr 26 10:52:55 2016
/////////////////////////////////////////////////////////////


module b01 ( line1, line2, reset, outp, overflw, clock, test_si, test_so,
        test_se );
  input line1, line2, reset, clock, test_si, test_se;
  output outp, overflw, test_so;
  wire   N41, N42, N43, N44, N45, n5, n7, n8, n9, n10, n11, n12, n13, n14, n15,
         n16, n17, n18, n19, n20, n21, n22, n23, n24, n25, n29;
  wire   [2:0] stato;
  assign test_so = stato[2];

  FD2S stato_reg_0_ ( .D(N41), .TI(overflw), .TE(test_se), .CP(clock), .CD(n5),
        .Q(stato[0]), .QN(n29) );
  FD2S stato_reg_1_ ( .D(N42), .TI(n29), .TE(test_se), .CP(clock), .CD(n5),
        .Q(stato[1]), .QN(n9) );
  FD2S outp_reg ( .D(N44), .TI(test_si), .TE(test_se), .CP(clock), .CD(n5),
        .Q(outp) );
  IVI U7 ( .A(reset), .Z(n5) );
  EOI U9 ( .A(n12), .B(n13), .Z(N44) );
  ENI U10 ( .A(line1), .B(line2), .Z(n13) );
  ND2I U11 ( .A(stato[2]), .B(n8), .Z(n12) );
  ND2I U12 ( .A(n14), .B(n15), .Z(N43) );
  ENI U22 ( .A(n10), .B(stato[0]), .Z(n25) );
  ND2I U23 ( .A(line1), .B(line2), .Z(n20) );
  ND2I U24 ( .A(n11), .B(n8), .Z(n22) );
  NR2I U25 ( .A(n9), .B(stato[0]), .Z(n16) );
  NR2I U26 ( .A(line2), .B(line1), .Z(n21) );
  IVI U27 ( .A(n16), .Z(n8) );
  IVI U28 ( .A(n20), .Z(n10) );
  IVI U29 ( .A(n21), .Z(n11) );
  AN3 U30 ( .A(stato[0]), .B(n7), .C(stato[1]), .Z(N45) );
  FD2S stato_reg_2_ ( .D(N43), .TI(stato[1]), .TE(test_se), .CP(clock), .CD(n5), .Q(stato[2]), .QN(n7) );
  FD2S overflw_reg ( .D(N45), .TI(outp), .TE(test_se), .CP(clock), .CD(n5),
        .Q(overflw) );
  AO3 U33 ( .A(n17), .B(n7), .C(n18), .D(n19), .Z(N42) );
  AO2 U34 ( .A(n21), .B(n9), .C(stato[0]), .D(n11), .Z(n17) );
  ND3 U35 ( .A(n20), .B(n9), .C(stato[0]), .Z(n19) );
  AO7 U36 ( .A(n10), .B(n7), .C(n16), .Z(n18) );
  AO3 U37 ( .A(n7), .B(n22), .C(n23), .D(n24), .Z(N41) );
  ND3 U38 ( .A(n9), .B(n7), .C(n25), .Z(n23) );
  AO2 U39 ( .A(n10), .B(n16), .C(N45), .D(n20), .Z(n24) );
  AO3 U40 ( .A(stato[0]), .B(n11), .C(n9), .D(stato[2]), .Z(n15) );
  AO7 U41 ( .A(n16), .B(n10), .C(n7), .Z(n14) );
endmodule
      """
    object lexical extends StdNetlistLexical
    import lexical._
    import jp.ac.nara_k.info.verilog_netlist.parser.token.NetlistTokens.ErrorToken
    val tokenList = parse(tokens, b02_net)
    println(tokenList)
    assert(
      !tokenList.get.exists(_.isInstanceOf[ErrorToken])
    )
    object parser extends NetlistParsers
    val reader = new NetlistTokenReader(tokenList.get)
    val parseResult = parser.module(reader)
    println(parseResult)
  }
}
