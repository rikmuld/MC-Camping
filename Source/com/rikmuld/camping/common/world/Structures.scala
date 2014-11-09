package com.rikmuld.camping.common.world

import net.minecraft.world.World
import java.util.Random
import net.minecraft.block.material.Material
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.common.objs.tile.TileEntityTent
import net.minecraft.init.Blocks
import net.minecraft.block.Block
import com.rikmuld.camping.common.objs.entity.Camper

class HempGeneration extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, x: Int, y: Int, z: Int): Boolean = {
    for (l <- 0 until 20) {
      val i1 = (x + random.nextInt(4)) - random.nextInt(4)
      val j1 = y
      val k1 = (z + random.nextInt(4)) - random.nextInt(4)
      if (world.isAirBlock(i1, y, k1) && ((world.getBlock(i1 - 1, y - 1, k1).getMaterial() == Material.water) || (world.getBlock(i1 + 1, y - 1, k1).getMaterial() == Material.water) || (world.getBlock(i1, y - 1, k1 - 1).getMaterial() == Material.water) || (world.getBlock(i1, y - 1, k1 + 1).getMaterial() == Material.water))) {
        val l1 = random.nextInt(random.nextInt(4) + 1)
        if (Objs.hemp.canBlockStay(world, i1, j1, k1)) {
          world.setBlock(i1, j1, k1, Objs.hemp, l1, 2)
          if (l1 == 4) world.setBlock(i1, j1 + 1, k1, Objs.hemp, 5, 2)
        }
      }
    }
    true
  }
}

class CampsiteGeneration extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, rand: Random, x: Int, yCoord: Int, z: Int): Boolean = {
    var y = yCoord;
    while ((world.getBlock(x, y + 1, z) != Blocks.air) || (world.getBlock(x, y + 2, z) != Blocks.air)) {
      y += 1
    }
    if (!LocationIsValidSpawn(world, x - 1, y, z) || !LocationIsValidSpawn(world, x - 1, y, z + 2) || !LocationIsValidSpawn(world, x + 4, y, z + 2) || !LocationIsValidSpawn(world, x + 4, y, z) || !isValitSpawn(world, x, y, z, 6, 3)) return false
    world.setBlock(x + 0, y + 1, z + 1, Objs.campfire, 0, 2)
    world.setBlock(x + 2, y + 1, z + 1, Objs.tent, 2, 2)
    world.getTileEntity(x + 2, y + 1, z + 1).asInstanceOf[TileEntityTent].setRotation(3)
    world.getTileEntity(x + 2, y + 1, z + 1).asInstanceOf[TileEntityTent].setContends(1, TileEntityTent.BEDS, true, 0)
    world.spawnEntityInWorld(new Camper(world, x, y + 1, z))
    true
  }
  def isValitSpawn(world: World, x: Int, y: Int, z: Int, xLength: Int, zLength: Int): Boolean = {
    for (i <- 0 until xLength; j <- 0 until zLength if world.getBlock(x + xLength, y + 1, z + zLength) != Blocks.air) return false
    true
  }
  def LocationIsValidSpawn(world: World, i: Int, j: Int, k: Int): Boolean = (world.getBlock(i, j, k) == Blocks.grass)
}