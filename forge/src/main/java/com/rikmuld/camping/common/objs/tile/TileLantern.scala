package com.rikmuld.camping.common.objs.tile

import com.rikmuld.camping.core.LanternInfo
import net.minecraft.nbt.NBTTagCompound

class TileEntityLantern extends TileEntityMain {
  var burnTime: Int = _
  var update: Int = _
  
  override def readFromNBT(tag: NBTTagCompound) {
    burnTime = tag.getInteger("burnTime")
    super.readFromNBT(tag)
  }

  override def updateEntity() {
    if (!worldObj.isRemote) {
      update += 1
      if (update >= 10) {
        update = 0
        if(burnTime>0)burnTime -= 1
      }
      if (burnTime <= 0) {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, LanternInfo.LANTERN_OFF, 2)
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      }
    }
  }

  override def writeToNBT(tag: NBTTagCompound) {
    tag.setInteger("burnTime", burnTime)
    super.writeToNBT(tag)
  }
}
