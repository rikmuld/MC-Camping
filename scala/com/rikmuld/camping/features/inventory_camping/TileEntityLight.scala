package com.rikmuld.camping.features.inventory_camping

import com.rikmuld.corerm.tileentity.TileEntitySimple
import net.minecraft.util.ITickable

class TileEntityLight extends TileEntitySimple with ITickable {
  var tick: Int =
    0

  override def update(): Unit =
    if(!world.isRemote) {
      tick += 1

      if (tick > 10)
        world.setBlockToAir(pos)
    }
}