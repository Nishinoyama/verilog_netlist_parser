/////////////////////////////////////////////////////////////
// Created by: Synopsys DC Expert(TM) in wire load mode
// Version   : Q-2019.12-SP4
// Date      : Tue Sep 28 14:49:06 2021
/////////////////////////////////////////////////////////////


module b17 (x, y, cx, z, cz);
  input [1:0] x;
  input [1:0] y;
  input cx;
  output [1:0] z;
  output cz, cc;
  wire [1:0] n;
  wire nz;
  assign cz = n[0];
  ND2I P1 ( .A(x[0]), .B(y[0]), .Z(n[0]) );
  ND2I P2 ( .A(x[1]), .B(y[1]), .Z(n[1]) );
  ND2I P3 ( .A(n[0]), .B(cx), .Z(z[0]) );
  ND2I P4 ( .A(n[1]), .B(nz), .Z(z[1]) );
  FD2S P5 ( .D(n[0]), .Q(nz), .QN(cc));
endmodule

