package com.rikmuld.camping.client.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.core.TextInfo
import net.minecraft.util.StatCollector
import com.rikmuld.camping.common.inventory.gui.ContainerCampfire
import com.rikmuld.camping.common.objs.tile.TileEntityCampfire

class GuiCampfire(player: EntityPlayer, tile: IInventory) extends GuiContainerMain(new ContainerCampfire(player, tile)) {
  ySize = 120;

  def getTexture: String = TextureInfo.GUI_CAMPFIRE;
  def getName: String = "";
  def hasName: Boolean = false;
  override def drawGuiContainerForegroundLayer(par1: Int, par2: Int) {
    val time = tile.asInstanceOf[TileEntityCampfire].time;
    val timeLeft = (if (tile.asInstanceOf[TileEntityCampfire].color == 16) "" else ("§" + TextInfo.COLOURS_DYE(tile.asInstanceOf[TileEntityCampfire].color))) + java.lang.Integer.toString(time / 1200) + ":" + (if (java.lang.Integer.toString((time % 1200) / 20).length == 1) ("0" + java.lang.Integer.toString((time % 1200) / 20)) else (java.lang.Integer.toString((time % 1200) / 20)))
    fontRendererObj.drawString(StatCollector.translateToLocal(timeLeft), 92, 16, 4210752);
  }
}
