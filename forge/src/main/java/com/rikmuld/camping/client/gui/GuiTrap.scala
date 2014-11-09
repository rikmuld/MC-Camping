package com.rikmuld.camping.client.gui

import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.common.inventory.gui.ContainerKit
import net.minecraft.inventory.IInventory
import com.rikmuld.camping.common.inventory.gui.ContainerTrap

class GuiTrap(player: EntityPlayer, inv: IInventory) extends GuiContainerMain(new ContainerTrap(player, inv)) {
  ySize = 120

  def getTexture: String = TextureInfo.GUI_TRAP
  def getName: String = ""
  def hasName: Boolean = false
}