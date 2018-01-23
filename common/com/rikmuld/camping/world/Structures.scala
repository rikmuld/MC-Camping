package com.rikmuld.camping.world

import java.util.Random

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.objs.{BlockDefinitions, Objs}
import com.rikmuld.camping.objs.block.Hemp
import com.rikmuld.camping.objs.entity.{Camper, Campsite}
import com.rikmuld.camping.objs.tile.{TileEntityTent, TileTent}
import com.rikmuld.corerm.objs.blocks.WithInstable
import com.rikmuld.corerm.utils.WorldBlock.{BlockData, IMBlockData}
import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HempGen extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos: BlockPos): Boolean = {
    for (i <- 0 until CampingMod.config.hempGenMulti) {
      val posNew = pos.add(random.nextInt(8) - 4, 0, random.nextInt(8) - 4)
      var bd = (world, posNew)

      while (!world.canSeeSky(bd.pos)) {
        bd = bd.up
      }

      if (bd.isAir && (bd.down.north.material == Material.WATER || bd.down.south.material == Material.WATER || bd.down.west.material == Material.WATER || bd.down.east.material == Material.WATER)) {
        val age = random.nextInt(random.nextInt(4) + 1)
        if (Objs.hemp.asInstanceOf[WithInstable].canStay(world, bd.pos)) {
          bd.setState(Objs.hemp.getBlockState.getBaseState.withProperty(Hemp.AGE, age.asInstanceOf[java.lang.Integer]))
          if (age == BlockDefinitions.Hemp.GROWN_BIG_BOTTOM) bd.up.setState(Objs.hemp.getBlockState.getBaseState.withProperty(Hemp.AGE, (age + 1).asInstanceOf[java.lang.Integer]))
        }
      }
    }
    true
  }
}

class CampsiteGen extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos:BlockPos): Boolean = {
    var bd = (world, pos)

    while (bd.up.block != Blocks.AIR || bd.up.up.block != Blocks.AIR) bd = bd.up
    bd = bd.up

    if (!isValitSpawn(bd.west, 3, 2, 5)) return false

    bd.setState(Objs.campfireWood.getDefaultState)
    bd.south.south.setState(Objs.tent.getDefaultState)
    bd.south.south.tile.asInstanceOf[TileTent].createStructure
    bd.south.south.tile.asInstanceOf[TileTent].setContends(1, TileEntityTent.BEDS, true, 0)

    val camper = new Camper(world)
    camper.setPosition(bd.west.x, bd.west.y, bd.west.z)
    camper.setCampsite(Some(new Campsite(camper, bd.west.pos, bd.south.south.pos)))
    world.spawnEntity(camper)

    true
  }
  def isValitSpawn(bd:BlockData, xLength: Int, yLength:Int, zLength: Int): Boolean = {
    for (x <- 0 until xLength; y <- 0 until yLength; z <- 0 until zLength; if (!(bd.nw(bd.relPos(x, y, z)).block == Blocks.AIR || bd.nw(bd.relPos(x, y, z)).isReplaceable) 
        || !(bd.nw(bd.relPos(x, 0, z)).down.block == Blocks.GRASS || bd.nw(bd.relPos(x, 0, z)).down.block == Blocks.DIRT))) return false
    true
  }
}