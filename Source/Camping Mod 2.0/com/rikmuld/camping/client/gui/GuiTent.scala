package com.rikmuld.camping.client.gui

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.common.objs.tile.TileEntityTent
import com.rikmuld.camping.core.TextureInfo
import com.rikmuld.camping.core.GuiInfo
import net.minecraft.inventory.IInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.client.gui.inventory.GuiContainer
import com.rikmuld.camping.common.network.PacketSender
import com.rikmuld.camping.common.network.OpenGui
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.common.inventory.gui.ContainerTentChests
import com.rikmuld.camping.common.inventory.gui.ContainerTentLanterns
import java.util.ArrayList

class GuiTent(player: EntityPlayer, tile: IInventory) extends GuiScreen {
  var tent: TileEntityTent = tile.asInstanceOf[TileEntityTent]
  var canClick: Array[Boolean] = Array(false, false, false)

  protected override def actionPerformed(button: GuiButton): Unit = button.id match {
    case 0 => tent.removeAll()
    case 1 => tent.removeBed()
    case 2 => tent.removeLantern()
    case 3 => tent.removeChest()
  }
  override def doesGuiPauseGame(): Boolean = false
  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) {
    fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
  }
  override def drawScreen(mouseX: Int, mouseY: Int, partitialTicks: Float) {
    drawDefaultBackground()
    val guiLeft = (width - 255) / 2
    val guiTop = (height - 160) / 2
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT))
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, 255, 160)
    if (tent.lanterns == 0) drawTexturedModalRect(guiLeft + 32, guiTop + 78, 0, 160, 51, 53)
    if (tent.chests == 0) drawTexturedModalRect(guiLeft + 102, guiTop + 78, 51, 160, 51, 53)
    if (tent.beds == 0) drawTexturedModalRect(guiLeft + 172, guiTop + 78, 102, 160, 51, 53)
    GL11.glPushMatrix()
    drawCenteredString(fontRendererObj, "Space Left: " + tent.contends + "/" + tent.maxContends, (width / 2) - 45, guiTop + 10, 0)
    drawCenteredString(fontRendererObj, "Beds: " + tent.beds + "/" + tent.maxBeds, (width / 2) - 45, guiTop + 30, 0)
    drawCenteredString(fontRendererObj, "Lanterns: " + tent.lanterns + "/" + tent.maxLanterns, (width / 2) - 45, guiTop + 40, 0)
    drawCenteredString(fontRendererObj, "Chests: " + tent.chests + "/" + tent.maxChests, (width / 2) - 45, guiTop + 50, 0)
    GL11.glScalef(0.8F, 0.8F, 0.8F)
    drawCenteredString(fontRendererObj, "Manage Inventory", ((width / 2) * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
    drawCenteredString(fontRendererObj, "Manage Lantern", ((width / 2) * 1.25F).toInt - (80 * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
    drawCenteredString(fontRendererObj, "Manage Sleeping", ((width / 2) * 1.25F).toInt + (80 * 1.25F).toInt, (guiTop * 1.25F).toInt + (142 * 1.25F).toInt, 0)
    GL11.glPopMatrix()
    if (isPointInRegion(172, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) &&
      (tent.beds > 0)) {
      if (Mouse.isButtonDown(0) && canClick(0)) {
        mc.thePlayer.openGui(CampingMod, GuiInfo.GUI_TENT_SLEEP, tent.getWorldObj(), tent.xCoord, tent.yCoord, tent.zCoord)
      }
      if (!Mouse.isButtonDown(0)) canClick(0) = true
    } else {
      canClick(0) = false
    }
    if (isPointInRegion(102, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) &&
      (tent.chests > 0)) {
      if (Mouse.isButtonDown(0) && canClick(1)) {
        PacketSender.toServer(new OpenGui(GuiInfo.GUI_TENT_CHESTS, tent.xCoord, tent.yCoord, tent.zCoord))
      }
      if (!Mouse.isButtonDown(0)) {
        canClick(1) = true
      }
    } else canClick(1) = false
    if (isPointInRegion(32, 78, 51, 53, mouseX, mouseY, guiLeft, guiTop) && (tent.lanterns > 0)) {
      if (Mouse.isButtonDown(0) && canClick(2)) {
        PacketSender.toServer(new OpenGui(GuiInfo.GUI_TENT_LANTERN, tent.xCoord, tent.yCoord, tent.zCoord))
      }
      if (!Mouse.isButtonDown(0)) canClick(2) = true
    } else canClick(2) = false
    super.drawScreen(mouseX, mouseY, partitialTicks)
  }
  override def initGui() {
    super.initGui()
    val guiTop = (height - 160) / 2
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0, (width / 2) + 4, (guiTop + 10) - 2, 85, 10, "Clear All"))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(1, (width / 2) + 4, (guiTop + 30) - 2, 85, 10, "Remove Bed"))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(2, (width / 2) + 4, (guiTop + 40) - 2, 85, 10, "Remove Lantern"))
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(3, (width / 2) + 4, (guiTop + 50) - 2, 85, 10, "Remove Chest"))
  }
  private def isPointInRegion(x: Int, y: Int, width: Int, height: Int, pointX: Int, pointY: Int, guiLeft: Int, guiTop: Int): Boolean = {
    val pointXX = pointX - guiLeft
    val pointYY = pointY - guiTop
    (pointXX >= (x - 1)) && (pointXX < (x + width + 1)) && (pointYY >= (y - 1)) && (pointYY < (y + height + 1))
  }
  override def updateScreen() {
    super.updateScreen()
    if (!mc.thePlayer.isEntityAlive || mc.thePlayer.isDead) mc.thePlayer.closeScreen()
  }
}

class GuiTentSleeping(player: EntityPlayer, tile: IInventory) extends GuiScreen {
  var tent: TileEntityTent = tile.asInstanceOf[TileEntityTent]
  var canClick: Boolean = _

  protected override def actionPerformed(button: GuiButton): Unit = button.id match {
    case 0 => tent.sleep(mc.thePlayer)
  }
  override def doesGuiPauseGame(): Boolean = false
  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) {
    fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
  }
  override def drawScreen(mouseX: Int, mouseY: Int, partitialTicks: Float) {
    drawDefaultBackground()
    val guiLeft = (width - 97) / 2
    val guiTop = (height - 30) / 2
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_1))
    drawTexturedModalRect(guiLeft, guiTop, 0, 116, 97, 30)
    if (isPointInRegion(5, 5, 20, 20, mouseX, mouseY, guiLeft, guiTop)) {
      drawTexturedModalRect(guiLeft + 5, guiTop + 5, 75, 0, 20, 20)
      if (Mouse.isButtonDown(0) && canClick) {
        mc.thePlayer.openGui(CampingMod, GuiInfo.GUI_TENT, tent.getWorldObj(), tent.xCoord, tent.yCoord, tent.zCoord)
      }
      if (!Mouse.isButtonDown(0)) canClick = true
    } else canClick = false
    super.drawScreen(mouseX, mouseY, partitialTicks)
  }
  override def initGui() {
    super.initGui()
    val guiTop = (height - 30) / 2
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0, (width / 2) + -20, guiTop + 10, 61, 10, "Sleep"))
  }
  private def isPointInRegion(x: Int, y: Int, width: Int, height: Int, pointX: Int, pointY: Int, guiLeft: Int, guiTop: Int): Boolean = {
    val pointXX = pointX - guiLeft
    val pointYY = pointY - guiTop
    (pointXX >= (x - 1)) && (pointXX < (x + width + 1)) && (pointYY >= (y - 1)) && (pointYY < (y + height + 1))
  }
  override def updateScreen() {
    super.updateScreen()
    if (!mc.thePlayer.isEntityAlive || mc.thePlayer.isDead) mc.thePlayer.closeScreen()
  }
}

class GuiTentLanterns(player: EntityPlayer, inv: IInventory) extends GuiContainer(new ContainerTentLanterns(player, inv)) {
  var tent: TileEntityTent = inv.asInstanceOf[TileEntityTent]
  var canClick: Boolean = _

  ySize = 198

  override def drawCenteredString(fontRender: FontRenderer, text: String, x: Int, y: Int, color: Int) {
    fontRender.drawString(text, x - (fontRender.getStringWidth(text) / 2), y, color)
  }
  protected override def drawGuiContainerBackgroundLayer(partTicks: Float, mouseX: Int, mouseY: Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_1))
    drawTexturedModalRect(guiLeft + 63, guiTop, 0, 0, 50, 107)
    drawTexturedModalRect(guiLeft, guiTop + 104, 80, 165, xSize, 91)
    if (func_146978_c(63 + 15, 8, 20, 20, mouseX, mouseY)) {
      drawTexturedModalRect(guiLeft + 63 + 15, guiTop + 8, 75, 0, 20, 20)
      if (Mouse.isButtonDown(0) && canClick) {
        mc.thePlayer.openGui(CampingMod, GuiInfo.GUI_TENT, tent.getWorldObj, tent.xCoord, tent.yCoord, tent.zCoord)
      }
      if (!Mouse.isButtonDown(0)) canClick = true
    } else canClick = false
    val scale = tent.time.getScaledNumber(1500, 22).toInt
    drawTexturedModalRect(guiLeft + 13 + 63, (guiTop + 83) - scale, 50, 22 - scale, 25, 22)
  }
}

class GuiTentChests(player: EntityPlayer, inv: IInventory) extends GuiContainer(new ContainerTentChests(player, inv)) {
  var tent: TileEntityTent = inv.asInstanceOf[TileEntityTent]
  var slideState: Int = tent.slide
  var oldSLideState: Int = _
  var xBegin: Int = -1
  private var slideBegin: Int = _
  var mouseMouse: Boolean = _
  var canClick: Array[Boolean] = Array(false, false)

  ySize = 228
  xSize = 214

  protected override def drawGuiContainerBackgroundLayer(partTicks: Float, mouseX: Int, mouseY: Int) {
    oldSLideState = slideState
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_TENT_CONTENDS_2))
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    if (func_146978_c(15, 8, 20, 20, mouseX, mouseY)) {
      drawTexturedModalRect(guiLeft + 15, guiTop + 8, 214, 0, 20, 20)
      if (Mouse.isButtonDown(0) && canClick(0)) {
        mc.thePlayer.openGui(CampingMod, GuiInfo.GUI_TENT, tent.getWorldObj(), tent.xCoord, tent.yCoord, tent.zCoord)
      }
      if (!Mouse.isButtonDown(0)) canClick(0) = true
    } else canClick(0) = false
    if (func_146978_c(39 + slideState, 12, 15, 12, mouseX, mouseY)) {
      if (Mouse.isButtonDown(0) && canClick(1)) mouseMouse = true
      if (!Mouse.isButtonDown(0)) canClick(1) = true
    } else canClick(1) = false
    if (mouseMouse && (tent.chests > 2)) {
      if (xBegin == -1) {
        xBegin = mouseX
        slideBegin = slideState
      }
      slideState = (slideBegin + mouseX) - xBegin
      if (slideState < 0) slideState = 0
      if (slideState > 144) slideState = 144
    } else xBegin = -1
    if (!Mouse.isButtonDown(0)) mouseMouse = false
    drawTexturedModalRect(guiLeft + 39 + slideState, guiTop + 12, 234, if (tent.chests > 2) 12 else 0, 15, 12)
    if (slideState != oldSLideState) tent.setSlideState(slideState)
  }
}