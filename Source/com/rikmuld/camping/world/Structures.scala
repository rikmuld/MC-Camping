package com.rikmuld.camping.world

import com.rikmuld.camping.objs.Objs
import net.minecraft.world.World
import java.util.Random
import net.minecraft.block.material.Material
import net.minecraft.util.BlockPos
import com.rikmuld.corerm.misc.WorldBlock._
import com.rikmuld.camping.objs.block.Hemp
import com.rikmuld.corerm.objs.WithInstable
import com.rikmuld.camping.objs.Objs.ModBlocks.MetaLookup
import com.rikmuld.camping.objs.entity.Camper
import com.rikmuld.camping.objs.tile.TileEntityTent
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.camping.objs.tile.TileEntityTent

class HempGen extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos:BlockPos): Boolean = {
    var bd = (world, pos)
    for (tr <- 0 until 20) {
      bd = bd.nw(bd.relPos(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4)))
      if (bd.isAir && (bd.down.north.material == Material.water || bd.down.south.material == Material.water || bd.down.west.material == Material.water || bd.down.east.material == Material.water)) {
        val age = random.nextInt(random.nextInt(4) + 1)
        if (Objs.hemp.asInstanceOf[WithInstable].canStay(world, bd.pos)) {
          bd.setState(Objs.hemp.getBlockState.getBaseState.withProperty(Hemp.AGE, age))
          if (age == MetaLookup.Hemp.GROWN_BIG_BOTTOM) bd.up.setState(Objs.hemp.getBlockState.getBaseState.withProperty(Hemp.AGE, age + 1))
        }
      }
    }
    true
  }
}

class CampsiteGen extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos:BlockPos): Boolean = {
    var bd = (world, pos)
    
    while (bd.up.block != Blocks.air || bd.up.up.block != Blocks.air) bd = bd.up
    bd = bd.up
    
    if (!isValitSpawn(bd.west, 3, 2, 5)) return false

    bd.setState(Objs.campfire.getDefaultState)
    bd.south.south.setState(Objs.tent.getDefaultState)
    bd.south.south.tile.asInstanceOf[TileTent].createStructure
    bd.south.south.tile.asInstanceOf[TileTent].setContends(1, TileEntityTent.BEDS, true, 0)
    
    val camper = new Camper(world)
    camper.setPosition(bd.west.x, bd.west.y, bd.west.z)
    world.spawnEntityInWorld(camper)
    
    true
  }
  def isValitSpawn(bd:BlockData, xLength: Int, yLength:Int, zLength: Int): Boolean = {
    for (x <- 0 until xLength; y <- 0 until yLength; z <- 0 until zLength; if (!(bd.nw(bd.relPos(x, y, z)).block == Blocks.air || bd.nw(bd.relPos(x, y, z)).isReplaceable) || !(bd.nw(bd.relPos(x, 0, z)).down.block == Blocks.grass || bd.nw(bd.relPos(x, 0, z)).down.block == Blocks.dirt))) return false
    true
  }
}