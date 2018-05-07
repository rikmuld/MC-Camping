package com.rikmuld.camping.features.blocks.campfire.wood

import com.rikmuld.camping.Definitions.CampfireWood._
import com.rikmuld.corerm.tileentity.{TileEntitySimple, TileEntityTicker}
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class TileEntityCampfireWoodOff extends TileEntitySimple with TileEntityTicker {
  registerTicker(updateLight, 3)

  def updateLight() {
    if (!world.isRemote) {
      val light = getBlock.getInt(world, pos, STATE_LIGHT)

      if (light > 0)
        getBlock.setState(world, pos, STATE_LIGHT, Math.max(0, light - 1))
    }
  }

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean =
    oldState.getBlock != newState.getBlock
}