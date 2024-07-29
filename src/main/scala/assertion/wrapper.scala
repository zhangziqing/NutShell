
package assertion

import chisel3._
import chisel3.util._
import nutcore._
import top.Settings
import chisel3.util.experimental.BoringUtils
trait HasDebugParameter {
    val useAssertion = Settings.get("UseAssertion")
    val useILA = !useAssertion
}

class CacheCheckerWrapper(implicit val cacheConfig: CacheConfig)
  extends Module with HasNutCoreParameter with HasDebugParameter{
    val io = IO(new Bundle{
        val stage1Out = DecoupledMon(Flipped(new Stage1IO))
        val stage2In  = DecoupledMon(Flipped(new Stage1IO))
        val stage2Out = DecoupledMon(Flipped(new Stage2IO))
        val stage3In  = DecoupledMon(Flipped(new Stage2IO))
        val flush = Input(UInt(2.W))
        val memReqValid = Input(Bool())
    })
    val mmio = WireInit(false.B)
    val hit  = WireInit(false.B)
    val miss = WireInit(false.B)
    val probe   = WireInit(false.B)
    val stage3MainState = WireInit(0.U(log2Up(9).W))
    val meta = WireInit(0.U.asTypeOf(new MetaBundle()))


    // val cacheCheckerIO = WireInit(0.U(asTypeOf()))
    if (useAssertion){
        val cacheChecker = Module(new CacheChecker)
        cacheChecker.io.clk := clock
        cacheChecker.io.cacheHit := hit
        cacheChecker.io.mmio := mmio
        cacheChecker.io.probe := probe
        cacheChecker.io.miss := miss
        cacheChecker.io.stage3MainState := stage3MainState
        cacheChecker.io.flush := io.flush
        cacheChecker.io.stage3MetaDirty := meta.dirty
        cacheChecker.io.stage3MetaValid := meta.valid
        cacheChecker.io.stage3MetaTag := meta.tag
        cacheChecker.io.memReqValid := io.memReqValid
    } else {
        println("no support")
    }
}

class AXICheckWrapper extends Module with HasNutCoreParameter with HasDebugParameter{
    val io = IO(new Bundle{
        val axi = new AXI4Monitor()
        val axi_resetn = Input(Reset())
    })

    if (useAssertion){
        val assertion = Module(new AXIChecker)
        assertion.io.clk := clock
        assertion.io.axi <> io.axi 
        assertion.io.axi_resetn := io.axi_resetn
    }
    // } else if (useILA){
    //     BoringUtils.addSource(io.axi, "ilaAXI")
    // }
}

class CSRWrapper extends Module with HasNutCoreParameter with HasDebugParameter {
    val io = IO(new Bundle {
        val clk = Input(Clock())
        val mstatus = Input(UInt(XLEN.W))
        val mepc = Input(UInt(XLEN.W))
        val mtvec = Input(UInt(XLEN.W))
        val mie = Input(UInt(XLEN.W))
        val mideleg = Input(UInt(XLEN.W))
        val medeleg = Input(UInt(XLEN.W))
        val mcause = Input(UInt(XLEN.W))
        val scause = Input(UInt(XLEN.W))
        val causeNO = Input(UInt(XLEN.W))
        val raiseExceptionVec = Input(UInt(16.W))
        val priviledgeMode = Input(UInt(2.W))
        val raiseIntr = Input(Bool())
        val raiseTrap = Input(Bool())
        val uRet = Input(Bool())
        val mRet = Input(Bool())
        val sRet = Input(Bool())
        val instValid = Input(Bool())
    })
    val stagedIO = io.getElements.map { case x => RegNext(x)}
    val csrChecker = Module(new CSRChecker)
    csrChecker.io.getElements.zip(stagedIO).map {
        case (x, y) => x := y
    }

    csrChecker.io.clk := io.clk

}