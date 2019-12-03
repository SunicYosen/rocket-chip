// Design by sunic

package rocketchip.fpga

import Chisel._
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.devices.debug.Debug
import freechips.rocketchip.diplomacy.LazyModule
import freechips.rocketchip.util.AsyncResetReg

class Top()(implicit p: Parameters) extends Module {
  
  val ldut = LazyModule(new FPGARocketSystem)
  val dut = Module(ldut.module)

  require(ldut.mem_axi4.size == 1)
  require(ldut.mmio_axi4.size == 1)

  val io = IO(new Bundle {
    val mem_axi4  = ldut.mem_axi4.head.cloneType
    val mmio_axi4 = ldut.mmio_axi4.head.cloneType
  })

  io.mem_axi4  <> ldut.mem_axi4.head
  io.mmio_axi4 <> ldut.mmio_axi4.head

  // Allow the debug ndreset to reset the dut, but not until the initial reset has completed
  dut.reset := reset | dut.debug.map { debug => AsyncResetReg(debug.ndreset) }.getOrElse(false.B)

  dut.dontTouchPorts()
  dut.tieOffInterrupts()
  Debug.tieoffDebug(dut.debug)
}
