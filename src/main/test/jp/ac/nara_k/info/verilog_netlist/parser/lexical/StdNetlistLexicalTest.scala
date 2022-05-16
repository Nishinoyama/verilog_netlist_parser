package jp.ac.nara_k.info.verilog_netlist.parser.lexical

import org.scalatest.funsuite.AnyFunSuite

class StdNetlistLexicalTest extends AnyFunSuite {

  test("StdNetlistLexical.parseISCAS_b02") {
    val b02_net =
      """
        /////////////////////////////////////////////////////////////
        // Created by: Synopsys DC Expert(TM) in wire load mode
        // Version   : Q-2019.12-SP4
        // Date      : Thu Nov 11 09:32:59 2021
        /////////////////////////////////////////////////////////////


        module b02 ( reset, clock, linea, u, test_si, test_so, test_se );
          input reset, clock, linea, test_si, test_se;
          output u, test_so;
          wire   u, N33, N34, N35, N36, n4, n6, n7, n8, n9, n10, n11, n12, n13, n14,
                 n15;
          wire   [2:0] stato;
          assign test_so = u;

          FD2S stato_reg_0_ ( .D(N33), .TI(test_si), .TE(test_se), .CP(clock), .CD(n4),
                .Q(stato[0]), .QN(n8) );
          FD2S stato_reg_2_ ( .D(N35), .TI(stato[1]), .TE(test_se), .CP(clock), .CD(n4), .Q(stato[2]), .QN(n6) );
          FD2S stato_reg_1_ ( .D(N34), .TI(stato[0]), .TE(test_se), .CP(clock), .CD(n4), .Q(stato[1]), .QN(n7) );
          FD2S u_reg ( .D(N36), .TI(n6), .TE(test_se), .CP(clock), .CD(n4), .Q(u) );
          IVI U6 ( .A(reset), .Z(n4) );
          NR2I U10 ( .A(linea), .B(stato[2]), .Z(n11) );
          NR2I U11 ( .A(stato[0]), .B(n12), .Z(n10) );
          EOI U12 ( .A(stato[2]), .B(linea), .Z(n12) );
          ND2I U14 ( .A(n8), .B(n6), .Z(n14) );
          IVI U18 ( .A(linea), .Z(n9) );
          AO4 U19 ( .A(n13), .B(n8), .C(n7), .D(n14), .Z(N34) );
          AO6 U20 ( .A(n9), .B(n7), .C(stato[2]), .Z(n13) );
          AO4 U21 ( .A(n10), .B(n7), .C(n11), .D(n8), .Z(N35) );
          AO4 U22 ( .A(stato[1]), .B(stato[0]), .C(stato[2]), .D(n15), .Z(N33) );
          AO2 U23 ( .A(n9), .B(n8), .C(linea), .D(n7), .Z(n15) );
          NR3 U24 ( .A(n6), .B(stato[1]), .C(stato[0]), .Z(N36) );
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
  }
}
