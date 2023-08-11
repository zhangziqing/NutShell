package ENCORE 

import chisel3._ 
import chisel3.util._
import nutcore._
import chisel3.util.experimental.BoringUtils


object MonitorBoringUtils{
  def assignSink[T<:Bundle](bundle:T, name:String):Unit = {
    val monSeq = bundle.getElements
    monSeq.zipWithIndex.foreach{case (data,i) => 
      val temp = WireInit(0.U.asTypeOf(data))
      BoringUtils.addSink(temp, f"ila_$name$i")
      data := temp
    }
  } 
  def genSource[T<:Bundle](bundle:T, name:String):Unit = {
    val monSeq = bundle.getElements
    monSeq.zipWithIndex.foreach{case (data,i) => 
      BoringUtils.addSource(data, f"ila_$name$i")
    }
  }
}

class ENCOREBundle extends Bundle with HasNutCoreParameter;
class ENCOREInstrCommitIO extends ENCOREBundle{
  val valid    = Bool()
  val skip     = Bool()
  val isRVC    = Bool()
  val rfWen    = Bool()
  val rfDest   = UInt(5.W)
  val rfData   = UInt(XLEN.W)
  val pc       = UInt(64.W)
  val instr    = UInt(32.W)
}

class ENCOREArchEventIO extends ENCOREBundle {
  val intrNO        = UInt(32.W)
  val cause         = UInt(32.W)
  val exceptionPC   = UInt(64.W)
}

class ENCOREArchIntRegStateIO extends ENCOREBundle {
  val gpr_0 = UInt(64.W)
  val gpr_1 = UInt(64.W)
  val gpr_2 = UInt(64.W)
  val gpr_3 = UInt(64.W)
  val gpr_4 = UInt(64.W)
  val gpr_5 = UInt(64.W)
  val gpr_6 = UInt(64.W)
  val gpr_7 = UInt(64.W)
  val gpr_8 = UInt(64.W)
  val gpr_9 = UInt(64.W)
  val gpr_10 = UInt(64.W)
  val gpr_11 = UInt(64.W)
  val gpr_12 = UInt(64.W)
  val gpr_13 = UInt(64.W)
  val gpr_14 = UInt(64.W)
  val gpr_15 = UInt(64.W)
  val gpr_16 = UInt(64.W)
  val gpr_17 = UInt(64.W)
  val gpr_18 = UInt(64.W)
  val gpr_19 = UInt(64.W)
  val gpr_20 = UInt(64.W)
  val gpr_21 = UInt(64.W)
  val gpr_22 = UInt(64.W)
  val gpr_23 = UInt(64.W)
  val gpr_24 = UInt(64.W)
  val gpr_25 = UInt(64.W)
  val gpr_26 = UInt(64.W)
  val gpr_27 = UInt(64.W)
  val gpr_28 = UInt(64.W)
  val gpr_29 = UInt(64.W)
  val gpr_30 = UInt(64.W)
  val gpr_31 = UInt(64.W)
}


class ENCOREInstrCommit extends Module { 
  val io = IO(Input(new ENCOREInstrCommitIO))
  MonitorBoringUtils.genSource(io, "instr_commit_")
}


class ENCOREArchEvent extends Module {
  val io = IO(Input(new ENCOREArchEventIO))
  MonitorBoringUtils.genSource(io, "arch_event_")
}

class ENCOREArchIntRegState extends Module {
  val io = IO(Input(new ENCOREArchIntRegStateIO))
  MonitorBoringUtils.genSource(io, "arch_int_state_")
}
