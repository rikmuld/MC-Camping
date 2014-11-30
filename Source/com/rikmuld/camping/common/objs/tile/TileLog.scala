package com.rikmuld.camping.common.objs.tile

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import com.rikmuld.camping.common.objs.entity.EntityMountableBlock
import com.rikmuld.corerm.common.objs.tile.TileEntityWithRotation

class TileEntityLog extends TileEntityWithRotation {
  var update: Boolean = true
  var mountable: EntityMountableBlock = _

  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1)
  override def updateEntity() {
    if (update) {
      mountable = new EntityMountableBlock(worldObj, xCoord, yCoord, zCoord)
      worldObj.spawnEntityInWorld(mountable)
      update = false
    }
  }
}