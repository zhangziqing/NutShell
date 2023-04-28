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

class CacheCheckerWrapper(implicit val cacheConfig: CacheConfig) extends Module with HasNutCoreParameter with HasDebugParameter{
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