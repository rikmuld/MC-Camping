package com.rikmuld.camping.common.objs.tile

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.init.Blocks
import net.minecraft.inventory.ISidedInventory
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import com.rikmuld.corerm.common.network.PacketSender
import com.rikmuld.corerm.common.network.TileData
import com.rikmuld.camping.misc.Bounds
import com.rikmuld.camping.common.network.BoundsData
import com.rikmuld.corerm.common.objs.tile.TileEntityMain

class TileEntityWithBounds extends TileEntityMain {
  var bounds: Bounds = _
  var baseX: Int = _
  var baseY: Int = _
  var baseZ: Int = _
  private var update: Boolean = _

  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    baseX = tag.getInteger("baseX")
    baseY = tag.getInteger("baseY")
    baseZ = tag.getInteger("baseZ")
    if (tag.hasKey("xMin")) setBounds(Bounds.readBoundsToNBT(tag))
  }
  def setBaseCoords(x: Int, y: Int, z: Int) {
    baseX = x
    baseY = y
    baseZ = z
    sendTileData(0, true, x, y, z)
  }
  def setBounds(bounds: Bounds) {
    this.bounds = bounds
    update = true
  }
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 0) {
      baseX = data(0)
      baseY = data(1)
      baseZ = data(2)
    }
  }
  override def updateEntity() {
    if (!worldObj.isRemote && update) {
      PacketSender.toClient(new BoundsData(bounds, xCoord, yCoord, zCoord))
      update = false
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("baseX", baseX)
    tag.setInteger("baseY", baseY)
    tag.setInteger("baseZ", baseZ)
    if (bounds != null) bounds.writeBoundsToNBT(tag)
  }
}