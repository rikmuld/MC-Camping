package com.rikmuld.camping.features.blocks.tent

import com.rikmuld.corerm.gui.container.{ContainerSimple, ContainerTabbed}
import net.minecraft.entity.player.EntityPlayer

class ContainerTentNew(player: EntityPlayer, tile: TileEntityTent) extends ContainerSimple[TileEntityTent](player) with ContainerTabbed {
  override def getID: String =
    "container_tent"

  override def initIInventory: TileEntityTent =
    tile

  override def addInventorySlots(): Unit = {

  }
}
