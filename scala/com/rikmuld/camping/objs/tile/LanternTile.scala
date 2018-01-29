package com.rikmuld.camping.objs.tile

import com.rikmuld.camping.objs.BlockDefinitions
import com.rikmuld.camping.objs.block.Lantern
import com.rikmuld.corerm.tileentity.TileEntitySimple
import com.rikmuld.corerm.utils.WorldBlock._
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable

class TileLantern extends TileEntitySimple with ITickable {
  var burnTime: Int = _
  var ticker: Int = _

  override def readFromNBT(tag: NBTTagCompound) {
    burnTime = tag.getInteger("burnTime")
    super.readFromNBT(tag)
  }
  override def update {
    if (!world.isRemote) {
      ticker += 1
      if (ticker >= 10) {
        ticker = 0
        if (burnTime > 0) burnTime -= 1
      }
      if (burnTime <= 0 && bd.meta == BlockDefinitions.Lantern.ON) {
        bd.setMeta(BlockDefinitions.Lantern.OFF)
        bd.update
      }
    }
  }
  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound =  {
    tag.setInteger("burnTime", burnTime)
    super.writeToNBT(tag)
  }
  def isLit = bd.state.getValue(Lantern.LIT).asInstanceOf[Boolean]
  def isTop = bd.state.getValue(Lantern.TOP).asInstanceOf[Boolean]
}