package com.rikmuld.camping.features.blocks.campfire.cook

import java.util.Random

import com.rikmuld.camping.Definitions.CampfireCook._
import com.rikmuld.camping.features.blocks.campfire.BlockCampfire
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks._
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class BlockCampfireCook(modId:String, info:ObjDefinition) extends BlockRM(modId, info) {
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB =
    BlockCampfire.CAMPFIRE_COOK_BOUNDS

  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int =
    if(getBool(state, STATE_ON)) 15 else 0

  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state:IBlockState, world: World, pos:BlockPos, random: Random): Unit =
    if (getBool(state, STATE_ON))
      for (_ <- 0 until 3)
        BlockCampfire.particleAnimation(world, pos, 16, random, flame = true)
}