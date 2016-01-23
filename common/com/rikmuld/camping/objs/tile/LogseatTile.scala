package com.rikmuld.camping.objs.tile

import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.camping.objs.entity.Mountable
import net.minecraft.util.AxisAlignedBB
import net.minecraftforge.fml.relauncher.SideOnly
import com.rikmuld.camping.objs.entity.Mountable
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.world.World
import net.minecraft.server.gui.IUpdatePlayerListBox


class TileLogseat extends RMTile with IUpdatePlayerListBox{
  var mountable: Mountable = _

  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = AxisAlignedBB.fromBounds(pos.getX, pos.getY, pos.getZ, pos.getX+ 1, pos.getY + 1, pos.getZ + 1)
  override def update {
    if(mountable==null){
      mountable = new Mountable(worldObj)
      mountable.setPos(pos)
      worldObj.spawnEntityInWorld(mountable)
    }
  }
}