package com.rikmuld.camping.features.general.world

import java.util.Random

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.features.general.world.structures.{StructureCampsite, StructureHemp}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome.TempCategory
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.fml.common.IWorldGenerator

class WorldGenerator extends IWorldGenerator {
  val hemp = new StructureHemp
  val camp = new StructureCampsite

  override def generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider) {
    if (CONFIG.hasWorldGen) world.provider.getDimension match {
      case -1 => generateNether(world, random, chunkX * 16 + 8, chunkZ * 16 + 8)
      case 0 => generateSurface(world, random, chunkX * 16 + 8, chunkZ * 16 + 8)
      case 1 => generateEnd(world, random, chunkX * 16 + 8, chunkZ * 16 + 8)
      case _ =>
    }
  }

  private def generateEnd(world: World, random: Random, blockX: Int, blockZ: Int): Unit =
    Unit

  private def generateNether(world: World, random: Random, blockX: Int, blockZ: Int): Unit =
    Unit

  private def generateSurface(world: World, random: Random, blockX: Int, blockZ: Int): Unit = {
    if (CONFIG.worldGenHemp) {
      hemp.generate(world, random, new BlockPos(blockX, 50, blockZ))
    }

    if (BiomeDictionary.hasType(world.getBiomeForCoordsBody(new BlockPos(blockX, 0, blockZ)), BiomeDictionary.Type.FOREST) && (world.getBiomeForCoordsBody(new BlockPos(blockX, 0, blockZ)).getTempCategory == TempCategory.MEDIUM)) {
      if (random.nextInt(Math.max(1, CONFIG.campsiteRareness)) == 0) {
        if (CONFIG.worldGenCampsite) {
          camp.generate(world, random, new BlockPos(blockX - 4 + random.nextInt(8), 50, blockZ - 4 + random.nextInt(8)))
        }
      }
    }
  }
}