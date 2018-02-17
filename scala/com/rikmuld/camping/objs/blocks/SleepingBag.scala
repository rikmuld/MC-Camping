package com.rikmuld.camping.objs.blocks

import java.util.Random

import com.rikmuld.camping.objs.Definitions.SleepingBag._
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks._
import com.rikmuld.corerm.utils.MathUtils
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayer.SleepResult
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.init.Biomes
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}

import scala.collection.JavaConversions._

object SleepingBag {
  final val BOUNDS =
    new AxisAlignedBB(0, 0, 0, 1, 1f/16f, 1)
}

class SleepingBag(modId:String, info: ObjDefinition) extends BlockRM(modId, info) {
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB =
    SleepingBag.BOUNDS

  override def onBlockPlacedBy(world:World, pos:BlockPos, state:IBlockState, entity:EntityLivingBase, stack:ItemStack) {
    super.onBlockPlacedBy(world, pos, state, entity, stack)

    val facing = entity.getHorizontalFacing
    val headPos = pos.offset(facing)

    if (canPlaceBlockAt(world, headPos)) {
      world.setBlockState(headPos, getDefaultState)

      setState(world, headPos, "facing", facing)
      setState(world, headPos, STATE_IS_HEAD, true)
    } else
      world.destroyBlock(pos, true)
  }

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
      trySleep(world,
        if(getBool(state, STATE_IS_HEAD)) pos
        else pos.offset(getFacing(state)), player)
    else
      true

  def trySleep(world: World, pos: BlockPos, player: EntityPlayer): Boolean = {
    if(!world.provider.canRespawnHere || world.getBiomeForCoordsBody(pos) == Biomes.HELL) {
      world.destroyBlock(pos, false)
      world.newExplosion(null, pos.getX + 0.5, pos.getY + 0.5, pos.getZ + 0.5, 5, true, true)

      return true
    }

    if (getBool(world.getBlockState(pos), STATE_IS_OCCUPIED) && getPlayerInBed(world, pos).isDefined) {
      player.sendMessage(new TextComponentTranslation("tile.bed.occupied", new Object))

      return true
    }

    setState(world, pos, STATE_IS_OCCUPIED, false)

    player.trySleep(pos) match {
      case SleepResult.OK =>
        setState(world, pos, STATE_IS_OCCUPIED, true)
      case SleepResult.NOT_POSSIBLE_NOW =>
        player.sendMessage(new TextComponentTranslation("tile.bed.noSleep", new Object))
      case SleepResult.NOT_SAFE =>
        player.sendMessage(new TextComponentTranslation("tile.bed.noSafe", new Object))
      case SleepResult.TOO_FAR_AWAY =>
        player.sendMessage(new TextComponentTranslation("tile.bed.toFarAway", new Object))
      case _ =>
    }

    true
  }

  def getPlayerInBed(world: World, pos: BlockPos): Option[EntityPlayer] =
    world.playerEntities.find(player =>
      player.isPlayerSleeping && player.getPosition.equals(pos)
    )

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