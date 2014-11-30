package com.rikmuld.camping.common.objs.tile

import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.core.Objs
import net.minecraft.init.Blocks
import com.rikmuld.camping.core.SleepingBagInfo
import com.rikmuld.camping.core.SleepingBagInfo
import net.minecraft.world.World
import com.rikmuld.corerm.common.objs.tile.TileEntityWithRotation

class TileEntitySleepingBag extends TileEntityWithRotation {
  private var update: Int = _
  var sleepingPlayer: EntityPlayer = _
  val coordsHead = Array(Array(0, 0, 1), Array(-1, 0, 0), Array(0, 0, -1), Array(1, 0, 0))
  val coordsFoot = Array(Array(0, 0, -1), Array(1, 0, 0), Array(0, 0, 1), Array(-1, 0, 0))

  def breakHead() = worldObj.setBlock(xCoord + coordsFoot(rotation)(0), yCoord, zCoord + coordsFoot(rotation)(2), Blocks.air)
  private def breakIfNeeded() {
    if ((worldObj.getBlock(xCoord + coordsFoot(rotation)(0), yCoord, zCoord + coordsFoot(rotation)(2)) != Objs.sleepingBag) || (worldObj.getBlockMetadata(xCoord + coordsFoot(rotation)(0), yCoord, zCoord + coordsFoot(rotation)(2)) != 0)) {
      worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air)
    } else if (worldObj.getTileEntity(xCoord + coordsFoot(rotation)(0), yCoord, zCoord + coordsFoot(rotation)(2)).asInstanceOf[TileEntitySleepingBag].rotation != rotation) {
      worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air)
    }
  }
  private def setBedBottom() {
    if (worldObj.getBlock(xCoord + coordsHead(rotation)(0), yCoord, zCoord + coordsHead(rotation)(2)) == Blocks.air) {
      if (World.doesBlockHaveSolidTopSurface(worldObj, xCoord + coordsHead(rotation)(0), yCoord - 1, zCoord + coordsHead(rotation)(2))) {
        worldObj.setBlock(xCoord + coordsHead(rotation)(0), yCoord, zCoord + coordsHead(rotation)(2), Objs.sleepingBag, 1, 2)
        if (worldObj.getTileEntity(xCoord + coordsHead(rotation)(0), yCoord, zCoord + coordsHead(rotation)(2)) != null) {
          worldObj.getTileEntity(xCoord + coordsHead(rotation)(0), yCoord, zCoord + coordsHead(rotation)(2)).asInstanceOf[TileEntityWithRotation].rotation = rotation
        }
      } else worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air)
    } else if ((worldObj.getBlock(xCoord + coordsHead(rotation)(0), yCoord, zCoord + coordsHead(rotation)(2)) != Objs.sleepingBag) || (worldObj.getBlockMetadata(xCoord + coordsHead(rotation)(0), yCoord, zCoord + coordsHead(rotation)(2)) != SleepingBagInfo.FOOT)) {
      worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air)
    } else if (worldObj.getTileEntity(xCoord + coordsHead(rotation)(0), yCoord, zCoord + coordsHead(rotation)(2)).asInstanceOf[TileEntitySleepingBag].rotation != rotation) {
      worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air)
    }
  }
  override def updateEntity() {
    super.updateEntity()
    update += 1
    if (update > 5) {
      update = 0
      if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 0) setBedBottom()
      if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1) breakIfNeeded()
    }
    if (!worldObj.isRemote) if ((sleepingPlayer != null) && !sleepingPlayer.isPlayerSleeping) sleepingPlayer = null
  }
}
