// See LICENSE.SiFive for license details.
// See LICENSE.Berkeley for license details.

package sunic.fpga

import Chisel._
import freechips.rocketchip.config.Config
import freechips.rocketchip.subsystem._
import freechips.rocketchip.diplomacy._

class WithJtagDTMSystem extends freechips.rocketchip.subsystem.WithJtagDTM
class WithDebugSBASystem extends freechips.rocketchip.subsystem.WithDebugSBA
class WithDebugAPB extends freechips.rocketchip.subsystem.WithDebugAPB

class BaseConfig extends Config(
  new WithDefaultMemPort() ++
  new WithDefaultMMIOPort() ++
  new WithDefaultSlavePort() ++
  new WithTimebase(BigInt(1000000)) ++ // 1 MHz
  new WithDTS("freechips,rocketchip-unknown", Nil) ++
  new WithNExtTopInterrupts(2) ++
  new BaseSubsystemConfig()
)

class CacheConfig extends Config(  // 4 KiB SRAM
  new WithL1ICacheSets(32) ++
  new WithL1DCacheSets(32) ++
  new WithL1ICacheWays(4) ++
  new WithL1DCacheWays(4) ++
  new WithCacheBlockBytes(64)
)

class BaseFPGAConfig extends Config(
  new BaseConfig
)


class DefaultConfig extends Config(
  new WithNBigCores(1) ++ 
  new BaseConfig
)

class DefaultFPGAConfig extends Config(
  new WithNBigCores(1) ++ 
  new BaseFPGAConfig
)

class DefaultRV32FPGAConfig extends Config(
  new WithRV32 ++ 
  new DefaultFPGAConfig
)

class RV32WithoutFPUFPGAConfig extends Config(
  new WithRV32 ++
  new WithoutFPU ++ 
  new DefaultFPGAConfig
)

class RV32CacheFPGAConfig extends Config(
  new WithRV32 ++
  new CacheConfig ++
  new DefaultFPGAConfig
)

class RV32WithoutFPUCacheFPGAConfig extends Config(
  new WithRV32 ++
  new WithoutFPU ++ 
  new CacheConfig ++
  new WithNTrackersPerBank(1) ++
  new DefaultFPGAConfig
)
