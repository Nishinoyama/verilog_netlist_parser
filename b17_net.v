/////////////////////////////////////////////////////////////
// Created by: Synopsys DC Expert(TM) in wire load mode
// Version   : Q-2019.12-SP4
// Date      : Tue Sep 28 14:49:06 2021
/////////////////////////////////////////////////////////////


module b17 (datai, outp);
  input [4:1] datai;
  output outp;
  wire n1, n2;
  ND2I P2_U1283 ( .A(datai[1]), .B(datai[2]), .Z(n1) );
  ND2I P2_U895 ( .A(n1), .B(datai[3]), .Z(n2) );
  assign outp = n2;
endmodule

