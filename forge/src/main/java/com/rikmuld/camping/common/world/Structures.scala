package com.rikmuld.camping.common.world

import net.minecraft.world.World
import java.util.Random
import net.minecraft.block.material.Material
import com.rikmuld.camping.core.Objs

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