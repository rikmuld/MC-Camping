package com.rikmuld.camping.tileentity

import com.rikmuld.camping.objs.Definitions.Lantern
import com.rikmuld.camping.objs.Definitions.Lantern._
import com.rikmuld.camping.objs.blocks.Lantern
import com.rikmuld.corerm.tileentity.TileEntitySimple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class TileLantern extends TileEntitySimple with ITickable {
  private var burnTime: Int =
    _

  private var tick: Boolean =
    false

  override def readFromNBT(tag: NBTTagCompound): Unit = {
    burnTime = tag.getInteger("burnTime")
    super.readFromNBT(tag)
  }

  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound =  {
    tag.setInteger("burnTime", burnTime)
    super.writeToNBT(tag)
  }

  def setBurnTime(time: Int): Unit = {
    burnTime = time * 20
    tick = true
  }

  def getBurnTime: Int =
    burnTime

  def getLantern: Lantern =
    world.getBlockState(pos).getBlock.asInstanceOf[Lantern]

  override def update(): Unit =
    if (tick && !world.isRemote)
      if (burnTime > 0)
        burnTime -= 1
      else {
        getLantern.setState(world, pos, STATE_LIT, false)
        tick = false
      }

  override def init(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos): Unit =
    setBurnTime(Option(stack.getTagCompound).fold(
      if (stack.getItemDamage == Lantern.ON) 750
      else 0
    )(_.getInteger("time")))
}