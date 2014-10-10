package com.rikmuld.camping.common.objs.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB
import net.minecraft.init.Blocks
import java.util.ArrayList
import scala.collection.JavaConversions._
import com.rikmuld.camping.core.Utils._

class TileLight extends TileEntityMain {
  var tick: Int = _

  override def updateEntity() {
    if (!worldObj.isRemote) {
      tick += 1
      if (tick > 10) {
        var flag = true
        val players = worldObj.getEntitiesWithinAABB(classOf[EntityPlayer], AxisAlignedBB.getBoundingBox(xCoord - 2, 
          yCoord - 2, zCoord - 2, xCoord + 2, yCoord + 2, zCoord + 2)).asInstanceOf[ArrayList[EntityPlayer]]
        for (player <- players if player.hasLantarn()) {
          flag = false
        }
        if (flag) {
          worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air)
        }
        if (!flag) {
          tick = 0
        }
      }
    }
  }
}