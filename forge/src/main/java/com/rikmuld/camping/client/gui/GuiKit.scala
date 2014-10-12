package com.rikmuld.camping.client.gui

import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.common.inventory.gui.ContainerKit

class GuiKit(player:EntityPlayer) extends GuiContainerMain(new ContainerKit(player)) {
  ySize = 181;
  
  def getTexture: String = TextureInfo.GUI_KIT;
  def getName: String = "";
  def hasName: Boolean = false;
}