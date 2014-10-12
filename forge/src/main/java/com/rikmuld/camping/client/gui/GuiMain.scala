package com.rikmuld.camping.client.gui

import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import org.lwjgl.opengl.GL11
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.client.gui.FontRenderer

abstract class GuiContainerMain(container: Container) extends GuiContainer(container) {
  protected override def drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(getTexture))
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
  }
  override def drawGuiContainerForegroundLayer(i: Int, j: Int) = if (hasName) drawCenteredString(fontRendererObj, getName, xSize/2, 8, 4210752)
  def getTexture: String;
  def getName: String;
  def hasName: Boolean;
  override def drawCenteredString(fontRender:FontRenderer, text:String, x:Int, y:Int, color:Int) = fontRender.drawString(text, x - fontRender.getStringWidth(text) / 2, y, color);
}