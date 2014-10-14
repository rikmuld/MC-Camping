package com.rikmuld.camping.common.world

import net.minecraft.world.chunk.IChunkProvider
import cpw.mods.fml.common.IWorldGenerator
import com.rikmuld.camping.core.ConfigInfo
import net.minecraft.world.World
import java.util.Random
import com.rikmuld.camping.core.Objs

class WorldGenerator  extends IWorldGenerator {
  val hemp = new HempGeneration
		  
  var xBlock, yBlock, zBlock:Int = _
  
  override def generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
    if(Objs.config.hasWorldGen) world.provider.dimensionId match {
      case -1 => generateNether(world, random, chunkX * 16, chunkZ * 16)
      case 0 => generateSurface(world, random, chunkX * 16, chunkZ * 16)
      case 1 => generateEnd(world, random, chunkX * 16, chunkZ * 16)
    }
  }
  private def generateEnd(world: World, random: Random, blockX: Int, blockZ: Int) {}
  private def generateNether(world: World, random: Random, blockX: Int, blockZ: Int) {}
  private def generateSurface(world: World, random: Random, blockX: Int, blockZ: Int) {
    if(Objs.config.worldGenHemp) {
      for (i <- 0 until Math.max(1, Objs.config.hempGenMulti)) { 
        xBlock = blockX + random.nextInt(16)
        yBlock = random.nextInt(15) + 55
        zBlock = blockZ + random.nextInt(16)
        hemp.generate(world, random, xBlock, yBlock, zBlock)
      }
	}
  }
}