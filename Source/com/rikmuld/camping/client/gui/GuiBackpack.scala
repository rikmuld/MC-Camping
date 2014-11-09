package com.rikmuld.camping.client.gui

import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11
import net.minecraft.client.gui.inventory.GuiContainer
import com.rikmuld.camping.common.inventory.gui.ContainerBackpack

class GuiBackpack(player: EntityPlayer) extends GuiContainerMain(new ContainerBackpack(player)) {
  override def getTexture: String = TextureInfo.GUI_BAG;
  override def getName: String = "Hiking Bag";
  override def hasName: Boolean = true;
}