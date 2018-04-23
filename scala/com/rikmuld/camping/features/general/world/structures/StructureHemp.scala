package com.rikmuld.camping.features.general.world.structures

import java.util.Random

import com.rikmuld.camping.{CampingMod, Definitions}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class StructureHemp extends net.minecraft.world.gen.feature.WorldGenerator {
  override def generate(world: World, random: Random, pos: BlockPos): Boolean = {
    for (i <- 0 until CampingMod.CONFIG.hempGenMulti) {
      var posNew = pos.add(random.nextInt(8) - 4, 0, random.nextInt(8) - 4)

      while (!world.canSeeSky(posNew))
        posNew = posNew.up

      if (world.isAirBlock(posNew)) {
        val age = random.nextInt(4)

        if (CampingMod.OBJ.hemp.canStay(world, posNew)) {
          world.setBlockState(posNew, CampingMod.OBJ.hemp.getDefaultState)
          CampingMod.OBJ.hemp.setState(world, posNew, Definitions.Hemp.STATE_AGE, age)
        }
      }
    }
    true
  }
}