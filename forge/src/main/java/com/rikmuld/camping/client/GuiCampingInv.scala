package com.rikmuld.camping.client

import java.awt.Color

import scala.collection.JavaConversions._
import scala.collection.mutable.HashMap

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12

import com.rikmuld.camping.common.inventory.ContainerCampinv
import com.rikmuld.camping.common.inventory.ContainerCampinvCraft
import com.rikmuld.camping.common.network.OpenGui
import com.rikmuld.camping.common.network.PacketSender
import com.rikmuld.camping.core.GuiInfo
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.TextureInfo

import net.minecraft.block.material.MapColor
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class GuiCampinginv(var player: EntityPlayer) extends GuiContainer(new ContainerCampinv(player)) {
  var timer: Int = 5
  var itemRenderer: RenderItem = new RenderItem()
  var craftButton: GuiButton = _

  this.xSize = 220
  this.ySize = 160

  override def initGui() {
    super.initGui()
    craftButton = new GuiButton(1, guiLeft + xSize / 2 - 50, guiTop + 6, 100, 10, "Open Crafting Grit")
    craftButton.enabled = false
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0, 5, 5, 100, 10, "Minecraft Inventory"))
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(craftButton)
  }
  override def actionPerformed(button: GuiButton) {
    if (button.id == 0 && timer <= 0) PacketSender.toServer(new OpenGui(GuiInfo.GUI_INVENTORY))
    if (button.id == 1 && timer <= 0) PacketSender.toServer(new OpenGui(GuiInfo.GUI_CAMPINV_CRAFT))
  }
  protected override def drawGuiContainerBackgroundLayer(partTick: Float, mouseX: Int, mouseY: Int) {
    if (this.inventorySlots.asInstanceOf[ContainerCampinv].campinv.getStackInSlot(1) != null) {
      if (craftButton.enabled == false) craftButton.enabled = true
    } else if (craftButton.enabled == true) {
      craftButton.enabled = false
    }
    if (timer > 0) timer -= 1
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_CAMPINV))
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
    if (this.func_146978_c(8, 28, 16, 16, mouseX, mouseY)) itemRenderer.renderItemIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Objs.backpack), guiLeft + 8, guiTop + 28)
    if (this.func_146978_c(8, 46, 16, 16, mouseX, mouseY)) itemRenderer.renderItemIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Objs.knife), guiLeft + 8, guiTop + 46)
    if (this.func_146978_c(196, 28, 16, 16, mouseX, mouseY)) itemRenderer.renderItemIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Objs.lantern), guiLeft + 196, guiTop + 28)
    if (this.func_146978_c(196, 46, 16, 16, mouseX, mouseY)) itemRenderer.renderItemIntoGUI(fontRendererObj, mc.renderEngine, new ItemStack(Items.filled_map), guiLeft + 196, guiTop + 46)
  }
}
class GuiCampingInvCraft(var player: EntityPlayer) extends GuiContainer(new ContainerCampinvCraft(player)) {
  var timer: Int = 5

  this.xSize = 176
  this.ySize = 175

  override def initGui() {
    super.initGui()
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(0, 5, 5, 100, 10, "Minecraft Inventory"))
    this.buttonList.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(1, guiLeft + xSize / 2 - 50, guiTop + 6, 100, 10, "Camping Inventory"))
  }
  override def actionPerformed(button: GuiButton) {
    if (button.id == 0 && timer <= 0) PacketSender.toServer(new OpenGui(GuiInfo.GUI_INVENTORY))
    if (button.id == 1 && timer <= 0) PacketSender.toServer(new OpenGui(GuiInfo.GUI_CAMPINV))
  }
  protected override def drawGuiContainerBackgroundLayer(partTick: Float, mouseX: Int, mouseY: Int) {
    if (timer > 0) timer -= 1
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_CAMPINV_CRAFT))
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
  }
}

class GuiMapHUD extends GuiScreen {
  var colorData: HashMap[EntityPlayer, Array[Byte]] = new HashMap[EntityPlayer, Array[Byte]]()
  var posData: HashMap[EntityPlayer, Array[Int]] = new HashMap[EntityPlayer, Array[Int]]()
  private var rgbColors: Array[Int] = new Array[Int](16384)
  val textureID = GL11.glGenTextures()

  override def drawScreen(mouseX: Int, mouseY: Int, partTicks: Float) {
    GL11.glPushMatrix()
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glScalef(0.5F, 0.5F, 0.5F)
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.GUI_UTILS))
    drawTexturedModalRect((width * 2) - 133, 5, 0, 42, 128, 128)
    GL11.glScalef(2F, 2F, 2F)
    if (mc.thePlayer.worldObj.isRemote && (colorData != null) && (posData != null) && (colorData.contains(mc.thePlayer)) && (posData.contains(mc.thePlayer)) && (colorData(mc.thePlayer) != null) && (posData(mc.thePlayer) != null)) {
      for (i <- 0 until colorData(mc.thePlayer).length) {
        val colorIndex = colorData(mc.thePlayer)(i)
        if ((colorIndex / 4) == 0) rgbColors(i) = ((((i + (i / 128)) & 1) * 8) + 16) << 24
        else {
          val colorValue = MapColor.mapColorArray(colorIndex / 4).colorValue
          val heightFlag = colorIndex & 3
          var heigthDarkness = 220
          if (heightFlag == 2) heigthDarkness = 255
          if (heightFlag == 0) heigthDarkness = 180
          val Red = (((colorValue >> 16) & 255) * heigthDarkness) / 255
          val Green = (((colorValue >> 8) & 255) * heigthDarkness) / 255
          val Blue = ((colorValue & 255) * heigthDarkness) / 255
          rgbColors(i) = -16777216 | (Red << 16) | (Green << 8) | Blue
        }
      }
      val textureBuffer = BufferUtils.createByteBuffer(128 * 128 * 3)
      for (i <- 0 until (128 * 128)) {
        val pixel = rgbColors(i)
        val color = Color.decode(java.lang.Integer.toString(pixel))
        textureBuffer.put(color.getRed.toByte)
        textureBuffer.put(color.getGreen.toByte)
        textureBuffer.put(color.getBlue.toByte)
      }
      textureBuffer.flip()
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID)
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
      GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 128, 128, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, textureBuffer)
      GL11.glBegin(GL11.GL_QUADS)
      GL11.glTexCoord2f(0f, 0f)
      GL11.glVertex2f(width - (126f / 2), 12f / 2)
      GL11.glTexCoord2f(0f, 1f)
      GL11.glVertex2f(width - (126f / 2), 126f / 2)
      GL11.glTexCoord2f(1f, 1f)
      GL11.glVertex2f(width - (12 / 2), 126f / 2)
      GL11.glTexCoord2f(1f, 0f)
      GL11.glVertex2f(width - (12f / 2), 12f / 2)
      GL11.glEnd()
      for (i <- 0 until mc.theWorld.playerEntities.size) {
        val player = mc.theWorld.playerEntities.get(i).asInstanceOf[EntityPlayer]
        val scale = (57F / (64F * (Math.pow(2, posData(mc.thePlayer)(0))))).toFloat
        var xDivision = (scale * (player.posX - posData(mc.thePlayer)(1))).toInt
        var zDivision = (scale * (player.posZ - posData(mc.thePlayer)(2))).toInt
        if (xDivision > 57) xDivision = 57
        else if (xDivision < -57) xDivision = -57
        if (zDivision > 57) zDivision = 57
        else if (zDivision < -57) zDivision = -57
        if (mc.thePlayer == player) mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.RED_DOT))
        else mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.BLUE_DOT))
     
        GL11.glBegin(GL11.GL_QUADS)
        GL11.glTexCoord2f(0f, 0f)
        GL11.glVertex2f(((width - (69 / 2)) + (xDivision / 2)) - 3, ((69 / 2) + (zDivision / 2)) - 3)
        GL11.glTexCoord2f(0f, 1f)
        GL11.glVertex2f(((width - (69 / 2)) + (xDivision / 2)) - 3, (69 / 2) + (zDivision / 2) + 3)
        GL11.glTexCoord2f(1f, 1f)
        GL11.glVertex2f((width - (69 / 2)) + (xDivision / 2) + 3, (69 / 2) + (zDivision / 2) + 3)
        GL11.glTexCoord2f(1f, 0f)
        GL11.glVertex2f((width - (69 / 2)) + (xDivision / 2) + 3, ((69 / 2) + (zDivision / 2)) - 3)
        GL11.glEnd()
      }
    }
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    GL11.glPopMatrix()
  }
}