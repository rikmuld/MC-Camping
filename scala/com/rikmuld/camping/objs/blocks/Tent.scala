package com.rikmuld.camping.objs.blocks

import java.util

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.objs.Definitions.Tent._
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.tileentity.TileTent
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks.bounds.{BlockBounds, BlockBoundsStructure, BoundsStructure}
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.{IBlockAccess, World}

import scala.collection.JavaConversions._

object Tent {
  val tentStructure: Seq[BoundsStructure] = BoundsStructure.createWithRotation (
    new AxisAlignedBB(-0.5F, 0, 0, 1.5F, 1.5F, 3)
  )
}

//TODO drop contents of tent as well once implemented
class Tent(modId:String, info:ObjDefinition) extends BlockBoundsStructure(modId, info) {
  def getBoundsBlock: BlockBounds =
    ObjRegistry.tentBounds.asInstanceOf[BlockBounds]

  def getStructure(state: Option[IBlockState]): BoundsStructure =
    if(state.exists(_.getBlock == ObjRegistry.tent))
      Tent.tentStructure(getFacing(state.get).getHorizontalIndex)
    else
      Tent.tentStructure(CampingMod.proxy.eventsS.facing.getHorizontalIndex)

  override def onBlockPlacedBy(world: World, pos:BlockPos, state:IBlockState, entity: EntityLivingBase, stack: ItemStack): Unit = {
    val off = setState(state, STATE_ON, false)

    super.onBlockPlacedBy(world, pos, off, entity, stack)
  }

  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int =
    if(getBool(state, STATE_ON))
      15
    else
      0

  override def getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): util.List[ItemStack] =
    List(new ItemStack(ObjRegistry.tent, 1, world.getTileEntity(pos).asInstanceOf[TileTent].color))

//  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
//    if (!world.isRemote) {
//      val bd = (world, pos)
//      val stack = player.getHeldItem(hand)
//      val tile = bd.tile.asInstanceOf[TileTent]
//      if ((stack != null) && tile.addContends(stack)) {
//        stack.setCount(stack.getCount - 1)
//        if (stack.getCount < 0) PlayerUtils.setCurrentItem(player, ItemStack.EMPTY)
//        Objs.tentChanged.trigger(player.asInstanceOf[EntityPlayerMP], tile.getContends().toSeq)
//        return true
//      } else if ((stack != null) && (stack.getItem() == Items.DYE) && (bd.tile.asInstanceOf[TileTent].color != stack.getItemDamage)) {
//        bd.tile.asInstanceOf[TileTent].setColor(stack.getItemDamage)
//        stack.setCount(stack.getCount - 1)
//        return true
//      } else super.onBlockActivated(world, pos, state, player, hand, side, xHit, yHit, zHit)
//    }
//    true
//  }
}