package com.rikmuld.camping.objs.tile

import com.rikmuld.camping.objs.entity.Mountable
import com.rikmuld.corerm.tileentity.TileEntitySimple
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.fml.relauncher.{Side, SideOnly}


class TileLogseat extends TileEntitySimple with ITickable {
  var mountable: Mountable = _

  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = new AxisAlignedBB(pos.getX, pos.getY, pos.getZ, pos.getX+ 1, pos.getY + 1, pos.getZ + 1)
  override def update {
    if(mountable==null){
      mountable = new Mountable(world)
      mountable.setPos(pos)
      world.spawnEntity(mountable)
    }
  }
}