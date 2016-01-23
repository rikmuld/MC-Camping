package com.rikmuld.camping.world

import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.World
import java.util.Random
import net.minecraftforge.common.BiomeDictionary
import net.minecraft.world.biome.BiomeGenBase
import net.minecraft.world.biome.BiomeGenBase.TempCategory
import net.minecraftforge.fml.common.IWorldGenerator
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.CampingMod._
import net.minecraft.util.BlockPos

class WorldGenerator extends IWorldGenerator {
  val hemp = new HempGen
  val camp = new CampsiteGen

  var xBlock, yBlock, zBlock: Int = _

  override def generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
    if (config.hasWorldGen) world.provider.getDimensionId match {
      case -1 => generateNether(world, random, chunkX * 16, chunkZ * 16)
      case 0 => generateSurface(world, random, chunkX * 16, chunkZ * 16)
      case 1 => generateEnd(world, random, chunkX * 16, chunkZ * 16)
      case _ =>
    }
  }
  private def generateEnd(world: World, random: Random, blockX: Int, blockZ: Int) {}
  private def generateNether(world: World, random: Random, blockX: Int, blockZ: Int) {}
  private def generateSurface(world: World, random: Random, blockX: Int, blockZ: Int) {
    if (config.worldGenHemp) {
      for (i <- 0 until Math.max(1, config.hempGenMulti)) {
        xBlock = blockX + random.nextInt(16)
        yBlock = random.nextInt(15) + 55
        zBlock = blockZ + random.nextInt(16)
        hemp.generate(world, random, new BlockPos(xBlock, yBlock, zBlock))
      }
    }
    if (!config.coreOnly && BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(new BlockPos(blockX, 0, blockZ)), BiomeDictionary.Type.FOREST) && (world.getBiomeGenForCoords(new BlockPos(blockX, 0, blockZ)).getTempCategory == TempCategory.MEDIUM)) {
      if (random.nextInt(config.campsiteRareness) == 0) {
        if (config.worldGenCampsite) {
          xBlock = blockX + random.nextInt(16)
          yBlock = 50
          zBlock = blockZ + random.nextInt(16)
          camp.generate(world, random, new BlockPos(xBlock, yBlock, zBlock))
        }
      }
    }
  }
}