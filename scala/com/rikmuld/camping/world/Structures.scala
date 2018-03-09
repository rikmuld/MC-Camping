package com.rikmuld.camping.world

import java.util.Random

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.entity.{Camper, Campsite}
import com.rikmuld.camping.objs.Definitions
import com.rikmuld.camping.objs.blocks.Tent
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.tileentity.TileTent
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
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

        if (ObjRegistry.hemp.canStay(world, posNew)) {
          world.setBlockState(posNew, ObjRegistry.hemp.getDefaultState)
          ObjRegistry.hemp.setState(world, posNew, Definitions.Hemp.STATE_AGE, age)
        }
      }
    }
    true
  }
}

class CampsiteGen extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos:BlockPos): Boolean = {
    var posNew = pos

    while (world.getBlockState(posNew.up).getBlock != Blocks.AIR || world.getBlockState(posNew.up.up).getBlock != Blocks.AIR)
      posNew = posNew.up


    if (!Tent.tentStructure.head.canBePlaced(world, posNew)) return false

    world.setBlockState(posNew, ObjRegistry.campfireWoodOn.getDefaultState)

    world.setBlockState(posNew.south(2), ObjRegistry.tent.getDefaultState)
    world.getTileEntity(posNew.south(2)).asInstanceOf[TileTent].add(new ItemStack(ObjRegistry.sleepingBag))

    val camper = new Camper(world)
    camper.setPosition(posNew.west.getX, posNew.west.getY, posNew.west.getZ)
    camper.setCampsite(Some(new Campsite(camper, posNew.west, posNew.south.south)))
    world.spawnEntity(camper)

    true
  }
}