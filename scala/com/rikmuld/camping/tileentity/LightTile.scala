package com.rikmuld.camping.tileentity

import com.rikmuld.camping.inventory.camping.InventoryCamping
import com.rikmuld.corerm.tileentity.TileEntitySimple
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB

import scala.collection.JavaConversions._

class TileLight extends TileEntitySimple with ITickable {
  var tick: Int =
    0

  override def update(): Unit =
    if(!world.isRemote) {
      tick += 1

      if (tick > 10) {
        var flag = true
        val players = world.getEntitiesWithinAABB(classOf[EntityPlayer], new AxisAlignedBB(pos.getX - 2, pos.getY - 2, pos.getZ - 2, pos.getX + 2, pos.getY + 2, pos.getZ+ 2))

        for (player <- players) {
          InventoryCamping.refreshInventory(player)

          if(!InventoryCamping.getLantern.isEmpty)
            flag = false
        }

        if (flag)
          world.setBlockToAir(pos)
        else
          tick = 0
      }
    }
}