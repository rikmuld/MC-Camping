package com.rikmuld.camping.objs.blocks

import com.rikmuld.camping.objs.Definitions.Tent
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks.BlockRM
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockAccess, World}

class Tent(modId:String, info:ObjDefinition) extends BlockRM(modId, info) {
//  override def getCollisionBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB = {
//    TileEntityTent.bounds(getFacing(state)).getBlockCollision
//  }
//  override def breakBlock(world: World, pos:BlockPos, state:IBlockState) {
//    val tileFlag = Option((world, pos).tile)
//    if (tileFlag.isDefined&&tileFlag.get.isInstanceOf[TileTent]) {
//      var tile = tileFlag.get.asInstanceOf[TileTent]
//      if(tile.structures!=null&&tile.structures(getFacing(state))!=null)tile.structures(getFacing(state)).destroyStructure(world, tile.tracker(getFacing(state)))
//      if (!world.isRemote && !tile.dropped) {
//        tile.dropped = true
//        val stacks = new ArrayList[ItemStack]
//        dropBlockAsItem(world, pos, state, 1)
//        stacks.addAll(tile.getContends)
//        val stack = new ItemStack(this, 1, tile.color)
//        stacks.add(stack)
//        WorldUtils.dropItemsInWorld(world, stacks, pos)
//      }
//      super.breakBlock(world, pos, state)
//    }
//  }
//  override def canPlaceBlockAt(world: World, pos:BlockPos): Boolean = {
//    val bd = (world, pos)
//    ((bd.block == null) || bd.isReplaceable) && Objs.tentStructure(facingFlag).canBePlaced(world, new BoundsTracker(bd.x, bd.y, bd.z, TileEntityTent.bounds(facingFlag)))
//  }

  override def onBlockPlacedBy(world: World, pos:BlockPos, state:IBlockState, entity: EntityLivingBase, stack: ItemStack): Unit = {
    val off = setState(state, Tent.STATE_ON, false)
    super.onBlockPlacedBy(world, pos, off, entity, stack)
  }

//  override def quantityDropped(random: Random): Int = 0
//  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB = {
//    val tile = source.getTileEntity(pos).asInstanceOf[TileTent]
//    TileEntityTent.bounds(getFacing(state)).getBlockBounds
//  }
//  override def dropIfCantStay(world: World, pos: BlockPos) {
//    val tile = world.getTileEntity(pos).asInstanceOf[TileTent]
//    if (Option(tile.structures).isDefined && !tile.structures(getFacing(world.getBlockState(pos))).hadSolidUnderGround(world, tile.tracker(getFacing(world.getBlockState(pos))))) {
//      breakBlock(world, pos, world.getBlockState(pos))
//    }
//  }

  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int =
    if(getBool(state, Tent.STATE_ON))
      15
    else
      0
//
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