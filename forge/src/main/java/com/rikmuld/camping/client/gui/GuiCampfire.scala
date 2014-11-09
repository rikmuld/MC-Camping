package com.rikmuld.camping.client.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.core.TextInfo
import net.minecraft.util.StatCollector
import com.rikmuld.camping.common.inventory.gui.ContainerCampfire
import com.rikmuld.camping.common.objs.tile.TileEntityCampfire
import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook
import org.lwjgl.opengl.GL11
import net.minecraft.util.ResourceLocation
import net.minecraft.client.gui.inventory.GuiContainer
import com.rikmuld.camping.common.inventory.gui.ContainerCampfireCook

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

class GuiCampfireCook(player: EntityPlayer, tile: IInventory) extends GuiContainer(new ContainerCampfireCook(player, tile)) {
  var fire: TileEntityCampfireCook = tile.asInstanceOf[TileEntityCampfireCook]

  ySize = 188

  protected override def drawGuiContainerBackgroundLayer(mouseX: Float, mouseY: Int, partTicks: Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_CAMPFIRE_COOK))
    var scale = fire.getScaledCoal(40).toInt
    scale += 1
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    if (fire.equipment != null) {
      fire.equipment.drawGuiTexture(this)
    }
    drawTexturedModalRect(guiLeft + 66, (guiTop + 94) - scale, 176, 40 - scale, 44, scale)
    drawTexturedModalRect(guiLeft + 79, guiTop + 83, 79, 105, 18, 18)
    if (fire.equipment != null) {
      for (i <- 0 until fire.equipment.maxFood) {
        val scale2 = fire.getScaledcookProgress(10, i).toInt
        val isNotCooked = if (fire.getStackInSlot(i + 2) != null) fire.equipment.canCook(fire.getStackInSlot(i + 2)) else false
        drawTexturedModalRect(guiLeft + fire.equipment.slots(0)(i) + 16, guiTop + fire.equipment.slots(1)(i) + 2,
          223, 0, 3, 12)
        drawTexturedModalRect(guiLeft + fire.equipment.slots(0)(i) + 17, (guiTop + fire.equipment.slots(1)(i) + 13) - scale2,
          if (isNotCooked) 226 else 227, 11 - scale2, 1, scale2)
      }
    }
  }
  def getGuiLeft(): Int = guiLeft
  def getGuiTop(): Int = guiTop
}