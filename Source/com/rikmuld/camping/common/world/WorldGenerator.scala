package com.rikmuld.camping.common.world

import net.minecraft.world.chunk.IChunkProvider
import cpw.mods.fml.common.IWorldGenerator
import com.rikmuld.camping.core.ConfigInfo
import net.minecraft.world.World
import java.util.Random
import com.rikmuld.camping.core.Objs
import net.minecraftforge.common.BiomeDictionary
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.biome.BiomeGenBase.TempCategory

class WorldGenerator extends IWorldGenerator {
  val hemp = new HempGeneration
  val camp = new CampsiteGeneration

  var xBlock, yBlock, zBlock: Int = _

  override def generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
    if (Objs.config.hasWorldGen) world.provider.dimensionId match {
      case -1 => generateNether(world, random, chunkX * 16, chunkZ * 16)
      case 0 => generateSurface(world, random, chunkX * 16, chunkZ * 16)
      case 1 => generateEnd(world, random, chunkX * 16, chunkZ * 16)
      case _ =>
    }
  }
  private def generateEnd(world: World, random: Random, blockX: Int, blockZ: Int) {}
  private def generateNether(world: World, random: Random, blockX: Int, blockZ: Int) {}
  private def generateSurface(world: World, random: Random, blockX: Int, blockZ: Int) {
    if (Objs.config.worldGenHemp) {
      for (i <- 0 until Math.max(1, Objs.config.hempGenMulti)) {
        xBlock = blockX + random.nextInt(16)
        yBlock = random.nextInt(15) + 55
        zBlock = blockZ + random.nextInt(16)
        hemp.generate(world, random, xBlock, yBlock, zBlock)
      }
    }
    if (!Objs.config.coreOnly && BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(blockX, blockZ), BiomeDictionary.Type.FOREST) && (world.getBiomeGenForCoords(xBlock, zBlock).getTempCategory == TempCategory.MEDIUM)) {
      if (random.nextInt(Objs.config.campsiteRareness) == 0) {
        if (Objs.config.worldGenCampsite) {
          xBlock = blockX + random.nextInt(16)
          yBlock = 50
          zBlock = blockZ + random.nextInt(16)
          camp.generate(world, random, xBlock, yBlock, zBlock)
        }
      }
    }
  }
}