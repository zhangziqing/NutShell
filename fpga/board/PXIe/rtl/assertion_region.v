`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2023/04/20 21:59:54
// Design Name: 
// Module Name: test
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module assertion_region(
  input                 assert_clk,
  input                 assert_rstn,
  output  [ 31 : 0 ]    cnt_data,
  input [ 31 : 0 ]      cnt_addr,
  input                 cnt_req,
  output                cnt_ack,
  output                assertion_failed
);
endmodule
