package com.rikmuld.camping.features.blocks.sleeping_bag

import java.util.Random

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.SleepingBag._
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks._
import com.rikmuld.corerm.utils.MathUtils
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}

object BlockSleepingBag {
  final val BOUNDS =
    new AxisAlignedBB(0, 0, 0, 1, 1f/16f, 1)
}

class BlockSleepingBag(modId:String, info: ObjDefinition) extends BlockRM(modId, info) {
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB =
    BlockSleepingBag.BOUNDS

  override def onBlockPlacedBy(world:World, pos:BlockPos, state:IBlockState, entity:EntityLivingBase, stack:ItemStack) {
    super.onBlockPlacedBy(world, pos, state, entity, stack)

    val facing = entity.getHorizontalFacing
    val headPos = pos.offset(facing)

    world.setBlockState(headPos, getDefaultState)

    setState(world, headPos, "facing", facing)
    setState(world, headPos, STATE_IS_HEAD, true)
  }

  override def canPlaceBlockAt(world: World, pos: BlockPos): Boolean =
    super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.offset(CampingMod.proxy.eventsS.facing)) //TODO test thoroughly, the facing in multiplayer

  override def breakBlock(world: World, pos: BlockPos, state: IBlockState): Unit = {
    val isHead = getBool(state, STATE_IS_HEAD)
    val other = pos.offset(
      if(isHead) getFacing(state).getOpposite
      else getFacing(state)
    )

    if(world.getBlockState(other).getBlock == this)
      world.destroyBlock(other, true)

    super.breakBlock(world, pos, state)
  }

  override def quantityDropped(state:IBlockState, fortune:Int, random:Random): Int =
    MathUtils.toInt(!getBool(state, STATE_IS_HEAD))

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState,
                                player: EntityPlayer, hand:EnumHand, side: EnumFacing,
                                xHit: Float, yHit: Float, zHit: Float): Boolean =
    if(!world.isRemote)
      Utils.trySleep(isOccupied(world, pos), setBedOccupied(world, pos, player, _))(world,
        if(getBool(state, STATE_IS_HEAD)) pos
        else pos.offset(getFacing(state)), player)
    else
      true

  private def isOccupied(world: World, pos: BlockPos): Boolean =
    getBool(world.getBlockState(pos), STATE_IS_OCCUPIED)

  override def isBed(state:IBlockState, world:IBlockAccess, pos:BlockPos, player:Entity) =
    true

  override def setBedOccupied(blockAccess: IBlockAccess, pos:BlockPos, player:EntityPlayer, occupied: Boolean): Unit =
    blockAccess match {
      case world: World =>
        setState(world, pos, STATE_IS_OCCUPIED, occupied)
      case _ =>
    }

  override def getBedDirection(state:IBlockState, world:IBlockAccess, pos:BlockPos): EnumFacing =
    getFacing(world, pos)

  override def isBedFoot(world:IBlockAccess, pos:BlockPos): Boolean =
    !getBool(world, pos, STATE_IS_HEAD)
}