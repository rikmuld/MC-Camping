package com.rikmuld.camping.features.blocks.lantern

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.Lantern
import com.rikmuld.camping.Definitions.Lantern._
import com.rikmuld.corerm.tileentity.TileEntitySimple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object TileEntityLantern {
  def timeFromStack(stack: ItemStack): Int =
    Option(stack.getTagCompound).fold(
      if (stack.getItemDamage == Lantern.ON) 750
      else 0
    )(_.getInteger("time"))

  def stackFromTime(time: Int, count: Int): ItemStack = {
    val stack = new ItemStack(
      CampingMod.OBJ.lanternItem,
      count,
      if (time > 0) Lantern.ON else Lantern.OFF
    )

    if(time > 0) {
      val compound = new NBTTagCompound()
      compound.setInteger("time", time)
      stack.setTagCompound(compound)
    }

    stack
  }
}

class TileEntityLantern extends TileEntitySimple with ITickable {
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

  def getLantern: BlockLantern =
    world.getBlockState(pos).getBlock.asInstanceOf[BlockLantern]

  override def update(): Unit =
    if (tick && !world.isRemote)
      if (burnTime > 0)
        burnTime -= 1
      else {
        getLantern.setState(world, pos, STATE_LIT, false)
        tick = false
      }

  override def init(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos): Unit =
    setBurnTime(TileEntityLantern.timeFromStack(stack))
}