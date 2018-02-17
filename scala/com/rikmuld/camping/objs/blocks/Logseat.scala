package com.rikmuld.camping.objs.blocks

import com.rikmuld.camping.objs.Definitions.LogSeat
import com.rikmuld.camping.tileentity.TileLogseat
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks._
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}

class Logseat(modId:String, info: ObjDefinition) extends BlockRM(modId, info) {

  override def neighborChanged(state:IBlockState, world:World, pos:BlockPos, block:Block, fromPos: BlockPos): Unit =
    if(!world.isRemote) {
      val facing: EnumFacing =
        getState(state, "facing").get.asInstanceOf[EnumFacing]

      val right = facing.rotateY
      val left = right.getOpposite

      val rightState = world.getBlockState(pos.offset(right))
      val leftState = world.getBlockState(pos.offset(left))

      val rightLong =
        rightState.getBlock == this &&
          (
            getState(rightState, "facing").get == facing ||
            getState(rightState, "facing").get == facing.getOpposite
          )

      val leftLong =
        leftState.getBlock == this &&
          (
            getState(leftState, "facing").get == facing ||
            getState(leftState, "facing").get == facing.getOpposite
          )

      if(rightLong != getBool(state, LogSeat.STATE_RIGHT_LONG))
        setState(world, pos, LogSeat.STATE_RIGHT_LONG, rightLong)

      if(leftLong != getBool(state, LogSeat.STATE_LEFT_LONG))
        setState(world, pos, LogSeat.STATE_LEFT_LONG, leftLong)
    }

  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB = {
    val longLeft = getBool(state, LogSeat.STATE_LEFT_LONG)
    val longRight = getBool(state, LogSeat.STATE_RIGHT_LONG)

    getState(state, "facing").get.asInstanceOf[EnumFacing] match {
      case EnumFacing.NORTH =>
        new AxisAlignedBB(
          if(longLeft) 0 else 2f/16f,
          0,
          5f/16f,
          if(longRight) 1 else 14f/16f,
          6f/16f,
          11f/16f
        )
      case EnumFacing.SOUTH =>
        new AxisAlignedBB(
          if(longRight) 0 else 2f/16f,
          0,
          5f/16f,
          if(longLeft) 1 else 14f/16f,
          6f/16f,
          11f/16f
        )
      case EnumFacing.WEST =>
        new AxisAlignedBB(
          5f/16f,
          0,
          if(longRight) 0 else 2f/16f,
          11f/16f,
          6f/16f,
          if(longLeft) 1 else 14f/16f
        )
      case EnumFacing.EAST =>
        new AxisAlignedBB(
          5f/16f,
          0,
          if(longLeft) 0 else 2f/16f,
          11f/16f,
          6f/16f,
          if(longRight) 1 else 14f/16f
        )
    }
  }

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState,
                                player: EntityPlayer, hand:EnumHand, side: EnumFacing,
                                xHit: Float, yHit: Float, zHit: Float): Boolean = {

    if (!player.isRiding)//TODO improve mountable/client server stuff
      world.getTileEntity(pos).asInstanceOf[TileLogseat].mountable.tryAddPlayer(player)

    true
  }
}