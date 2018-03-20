package com.rikmuld.camping.features.blocks.campfire

import java.util.Random

import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object BlockCampfire {
  final val CAMPFIRE_WOOD_BOUNDS =
    new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)

  final val CAMPFIRE_COOK_BOUNDS =
    new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)

  @SideOnly(Side.CLIENT)
  def particleAnimation(world: World, pos: BlockPos, color: Int, random: Random, flame: Boolean): Unit = {
    val motionY = random.nextFloat * 0.025f + 0.025f

    val particleX = pos.getX + 0.35f + random.nextFloat * 0.3f
    val particleY = pos.getY + 0.1f + random.nextFloat * 0.15f
    val particleZ = pos.getZ + 0.35f + random.nextFloat * 0.3f

    if(flame)
      ParticleFlameColored.renderAt(world, particleX, particleY, particleZ, 0, motionY, 0,
        if(color == 15) 16
        else color
      )

    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX, particleY, particleZ, 0, 0.05, 0)
  }
}