package com.rikmuld.camping.objs.blocks

import com.rikmuld.camping.objs.Definitions.Lantern
import com.rikmuld.camping.objs.Definitions.Lantern._
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.tileentity.TileLantern
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks.BlockRM
import com.rikmuld.corerm.objs.items.ItemBlockRM
import net.minecraft.block.state.IBlockState
import net.minecraft.block.{Block, BlockFence, BlockLeaves}
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}

import scala.collection.JavaConversions._

class Lantern(modId: String, info: ObjDefinition) extends BlockRM(modId, info) {

  override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState,
                               entity: EntityLivingBase, stack: ItemStack):Unit =
    updateTop(world, pos)

  override def getDrops(world: IBlockAccess, pos: BlockPos,
                        state: IBlockState, fortune: Int): java.util.List[ItemStack] =
    world.getTileEntity(pos).asInstanceOf[TileLantern].getBurnTime match {
      case time if time < 20 =>
        List(new ItemStack(ObjRegistry.lantern, 1, Lantern.OFF))
      case time =>
        val lantern = new ItemStack(ObjRegistry.lantern, 1, Lantern.ON)
        val tag = new NBTTagCompound()

        tag.setInteger("time", time / 20)
        lantern.setTagCompound(tag)

        List(lantern)
    }

  override def canStay(world: World, pos: BlockPos): Boolean =
    validBottom(world, pos) || validTop(world, pos)

  override def dropIfCantStay(world: World, pos: BlockPos): Boolean = {
    val dropped = !super.dropIfCantStay(world, pos)

    if (dropped)
      updateTop(world, pos)

    dropped
  }

  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB =
    if(getBool(state, STATE_HANGING))
      new AxisAlignedBB(0.3F, 0, 0.3F, 0.7F, 1, 0.7F)
    else
      new AxisAlignedBB(0.3F, 0, 0.3F, 0.7F, 0.75625F, 0.7F)

  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int =
    if(getBool(state, STATE_LIT))
      15
    else
      0

  def updateTop(world: World, pos: BlockPos): Unit =
    setState(world, pos, STATE_HANGING, !super.canStay(world, pos))

  def validTop(world: World, pos:BlockPos): Boolean =
    world.isSideSolid(pos.up, EnumFacing.DOWN) || validBlock(world, pos.up)

  def validBottom(world: World, pos:BlockPos): Boolean =
    world.isSideSolid(pos.down, EnumFacing.UP) || validBlock(world, pos.down)

  def validBlock(world: World, pos: BlockPos): Boolean = {
    val block = world.getBlockState(pos).getBlock

    block.isInstanceOf[BlockFence] || block.isInstanceOf[BlockLeaves]
  }

  def getTile(world: IBlockAccess, pos: BlockPos): TileLantern =
    world.getTileEntity(pos).asInstanceOf[TileLantern]

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState,
                                player: EntityPlayer, hand:EnumHand, side: EnumFacing,
                                xHit: Float, yHit: Float, zHit: Float): Boolean = {

    if (!world.isRemote && !getBool(state, STATE_LIT)) {
      val stack = player.getHeldItem(hand)

      if (stack.getItem == Items.GLOWSTONE_DUST) {
        setState(world, pos, STATE_LIT, true)
        getTile(world, pos).setBurnTime(750)
        stack.setCount(stack.getCount - 1)

        true
      } else false
    } else false
  }
}

class LanternItem(modId: String, definition: ObjDefinition, block: Block)
  extends ItemBlockRM(modId, definition, block) {

  override def addInformation(stack: ItemStack,
                              player: World,
                              list: java.util.List[String],
                              tooltip: ITooltipFlag): Unit =
    if (stack.hasTagCompound)
      list.add("Burning time left: " +
        stack.getTagCompound.getInteger("time") + " seconds")
    else if(stack.getItemDamage == Lantern.ON)
      list.add("Burning time left: 750 seconds")
}