package com.rikmuld.camping.world

import java.util.Random

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.objs.{Definitions, Registry}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HempGen extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos: BlockPos): Boolean = {
    for (i <- 0 until CampingMod.config.hempGenMulti) {
      var posNew = pos.add(random.nextInt(8) - 4, 0, random.nextInt(8) - 4)

      while (!world.canSeeSky(posNew)) {
        posNew = posNew.up
      }

      if (world.isAirBlock(posNew)) {
        val age = random.nextInt(4)

        if (Registry.hemp.canStay(world, posNew)) {
          world.setBlockState(posNew, Registry.hemp.getDefaultState)
          Registry.hemp.setState(world, posNew, Definitions.Hemp.STATE_AGE, age)
        }
      }
    }
    true
  }
}

class CampsiteGen extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos:BlockPos): Boolean = {
//    var bd = (world, pos)
//
//    while (bd.up.block != Blocks.AIR || bd.up.up.block != Blocks.AIR) bd = bd.up
//    bd = bd.up
//
//    if (!isValitSpawn(bd.west, 3, 2, 5)) return false
//
//    bd.setState(Objs.campfireWood.getDefaultState)
//    bd.south.south.setState(Objs.tent.getDefaultState)
//    bd.south.south.tile.asInstanceOf[TileTent].createStructure
//    bd.south.south.tile.asInstanceOf[TileTent].setContends(1, TileEntityTent.BEDS, true, 0)
//
//    val camper = new Camper(world)
//    camper.setPosition(bd.west.x, bd.west.y, bd.west.z)
//    camper.setCampsite(Some(new Campsite(camper, bd.west.pos, bd.south.south.pos)))
//    world.spawnEntity(camper)
//
//    true
    true
  }
//  def isValitSpawn(world: World, pos: BlockPos, xLength: Int, yLength:Int, zLength: Int): Boolean = {
//    for (x <- 0 until xLength; y <- 0 until yLength; z <- 0 until zLength; if (!(bd.nw(bd.relPos(x, y, z)).block == Blocks.AIR || bd.nw(bd.relPos(x, y, z)).isReplaceable)
//        || !(bd.nw(bd.relPos(x, 0, z)).down.block == Blocks.GRASS || bd.nw(bd.relPos(x, 0, z)).down.block == Blocks.DIRT))) return false
//    true
//  }
}