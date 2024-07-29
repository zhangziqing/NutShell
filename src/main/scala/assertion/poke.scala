package assertion

import chisel3._
import chisel3.util._

// map the call operation to the signal enable
class PokeSimTime(name:String) extends BlackBox(Map("error_name"->name)) with HasBlackBoxInline{
    val io = IO(new Bundle{val en = Input(Bool())})
    setInline("PokeSimTime.v",s"""
    | import "DPI-C" function void poke_simtime(string c);
    | module PokeSimTime #(
    |   parameter error_name = "error"
    | )(
    |   input en
    |);
    | always_comb begin
    |   if (en) poke_simtime(error_name);
    | end
    | endmodule
    """.stripMargin)
}

object PokeSimTime {
    def apply(cond:Bool, name:String) = {
        val entity = Module(new PokeSimTime(name))
        entity.io.en := cond
        entity
    }
}