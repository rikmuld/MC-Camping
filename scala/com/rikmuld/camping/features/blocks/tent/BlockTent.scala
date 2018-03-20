package com.rikmuld.camping.features.blocks.tent

import java.util

import com.rikmuld.camping.Definitions.Tent._
import com.rikmuld.camping.EventsServer
import com.rikmuld.camping.Library.AdvancementInfo
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks.bounds.{BlockBounds, BlockBoundsStructure, BoundsStructure}
import com.rikmuld.corerm.utils.WorldUtils
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}

import scala.collection.JavaConversions._

object BlockTent {
  val tentStructure: Seq[BoundsStructure] = BoundsStructure.createWithRotation (
    new AxisAlignedBB(-0.5F, 0, 0, 1.5F, 1.5F, 3)
  )
}

class BlockTent(modId:String, info:ObjDefinition) extends BlockBoundsStructure(modId, info) {
  def getBoundsBlock: BlockBounds =
    ObjRegistry.tentBounds.asInstanceOf[BlockBounds]

  def getStructure(state: Option[IBlockState]): BoundsStructure =
    if(state.exists(_.getBlock == ObjRegistry.tent))
      BlockTent.tentStructure(getFacing(state.get).getHorizontalIndex)
    else
      BlockTent.tentStructure(EventsServer.getFacing.getHorizontalIndex)

  override def breakBlock(world: World, pos: BlockPos, state: IBlockState): Unit = {
    val tile = world.getTileEntity(pos).asInstanceOf[TileEntityTent]

    WorldUtils.dropItemsInWorld(world, tile.toStacks, pos)

    super.breakBlock(world, pos, state)
  }

  override def onBlockPlacedBy(world: World, pos:BlockPos, state:IBlockState, entity: EntityLivingBase, stack: ItemStack): Unit = {
    val off = setState(state, STATE_ON, false)

    super.onBlockPlacedBy(world, pos, off, entity, stack)
  }

  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int =
    if(getBool(state, STATE_ON))
      15
    else
      0

  override def isBed(state:IBlockState, world:IBlockAccess, pos:BlockPos, player:Entity) =
    true

  override def setBedOccupied(world: IBlockAccess, pos:BlockPos, player:EntityPlayer, occupied: Boolean): Unit =
    world match {
      case world: World =>
        world.getTileEntity(pos).asInstanceOf[TileEntityTent].setOccupied(occupied)
      case _ =>
    }

  override def getBedDirection(state:IBlockState, world:IBlockAccess, pos:BlockPos): EnumFacing =
    getFacing(world, pos) // TODO reverse sleeping bag texture in tent and then get opposite here

  override def isBedFoot(world:IBlockAccess, pos:BlockPos): Boolean =
    false

  override def getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): util.List[ItemStack] =
    List(new ItemStack(ObjRegistry.tent, 1, world.getTileEntity(pos).asInstanceOf[TileEntityTent].getColor))

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState,
                                player: EntityPlayer, hand: EnumHand, side: EnumFacing,
                                xHit: Float, yHit: Float, zHit: Float): Boolean = {

    val stack = player.getHeldItem(hand)
    val tile = world.getTileEntity(pos).asInstanceOf[TileEntityTent]

    if(tile.canAdd(stack)) {
      if (!world.isRemote && tile.add(stack)) {
        stack.setCount(stack.getCount - 1)
        TriggerHelper.trigger(AdvancementInfo.TENT_CHANGED, player, tile.toStacks)
      }

      true
    }

    else if (stack.getItem == Items.DYE && tile.getColor != stack.getItemDamage) {
      if (!world.isRemote) {
        tile.setColor(stack.getItemDamage)
        stack.setCount(stack.getCount - 1)
      }

      true
    }

    else super.onBlockActivated(world, pos, state, player, hand, side, xHit, yHit, zHit)
  }
}