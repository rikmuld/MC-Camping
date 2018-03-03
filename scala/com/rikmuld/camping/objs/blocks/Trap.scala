package com.rikmuld.camping.objs.blocks

import com.rikmuld.camping.objs.Definitions.Trap._
import com.rikmuld.camping.objs.blocks.Trap._
import com.rikmuld.camping.tileentity.TileTrap
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks.BlockRM
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}

object Trap {
  final val BOUNDS_OPEN =
    new AxisAlignedBB(0.21875f, 0, 0.21875f, 0.78125f, 0.1875f, 0.78125f)

  final val BOUNDS_CLOSED =
    new AxisAlignedBB(0.21875f, 0, 0.34375f, 0.78125f, 0.25f, 0.65f)
}

class Trap(modId:String, info: ObjDefinition) extends BlockRM(modId, info) {
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB =
    if(getBool(state, STATE_OPEN))
      BOUNDS_OPEN
    else
      BOUNDS_CLOSED

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState,
                                player: EntityPlayer, hand:EnumHand, side: EnumFacing,
                                xHit: Float, yHit: Float, zHit: Float): Boolean = {

    val tile = world.getTileEntity(pos).asInstanceOf[TileTrap]

    tile.lastPlayer = Some(player)

    if(!getBool(state, STATE_OPEN)){
      tile.setOpen(true)
      true
    } else super.onBlockActivated(world, pos, state, player, hand, side, xHit, yHit, zHit)
  }
}