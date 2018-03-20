package com.rikmuld.camping.features.general.world.structures

import java.util.Random

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.CampfireWood
import com.rikmuld.camping.features.blocks.tent.{BlockTent, TileEntityTent}
import com.rikmuld.camping.features.entities.camper.{Campsite, EntityCamper}
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class StructureCampsite extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos:BlockPos): Boolean = {
    var posNew = pos

    while (world.getBlockState(posNew.up).getBlock != Blocks.AIR || world.getBlockState(posNew.up.up).getBlock != Blocks.AIR)
      posNew = posNew.up

    posNew = posNew.up

    if (!BlockTent.tentStructure.head.canBePlaced(world, posNew.south(2))) return false

    val campfire = CampingMod.OBJ.campfireWoodOn
    world.setBlockState(posNew, campfire.setState(campfire.getDefaultState, CampfireWood.STATE_LIGHT, 15))

    //TODO sometimes seems to be rotated wrongly
    world.setBlockState(posNew.south(2), CampingMod.OBJ.tent.getDefaultState)
    world.getTileEntity(posNew.south(2)) match {
      case tent: TileEntityTent =>
        tent.add(new ItemStack(CampingMod.OBJ.sleepingBag))
      case _ =>
    }

    val camper = new EntityCamper(world)
    camper.setPosition(posNew.west.getX, posNew.west.getY, posNew.west.getZ)
    camper.setCampsite(Some(new Campsite(camper, posNew.west, posNew.south.south)))
    world.spawnEntity(camper)

    true
  }
}