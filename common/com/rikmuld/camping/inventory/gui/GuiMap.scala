package com.rikmuld.camping.inventory.gui

import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.client.GuiContainerSimple
import com.rikmuld.corerm.inventory.RMContainerItem
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.inventory._
import net.minecraft.nbt.NBTTagList
import com.rikmuld.corerm.inventory.SlotNoPickup
import net.minecraft.item.ItemStack
import java.util.ArrayList
import net.minecraft.inventory.Slot
import net.minecraft.nbt.NBTTagCompound
import com.rikmuld.corerm.inventory.RMInventoryItem
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.camping.misc.CookingEquipment
import net.minecraft.block.material.MapColor
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL12
import scala.collection.mutable.HashMap
import java.awt.Color

class GuiMapHUD extends GuiScreen {
  final val TEX_UTILS = new ResourceLocation(TextureInfo.GUI_UTILS)
  final val TEX_RED_DOT = new ResourceLocation(TextureInfo.RED_DOT)
  final val TEX_BLUE_DOT = new ResourceLocation(TextureInfo.BLUE_DOT)

  var colorData: HashMap[EntityPlayer, Array[Byte]] = new HashMap[EntityPlayer, Array[Byte]]()
  var posData: HashMap[EntityPlayer, Array[Int]] = new HashMap[EntityPlayer, Array[Int]]()
  private var rgbColors: Array[Int] = new Array[Int](16384)
  val textureID = GL11.glGenTextures()

  override def drawScreen(mouseX: Int, mouseY: Int, partTicks: Float) {
    GL11.glPushMatrix()
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glScalef(0.5F, 0.5F, 0.5F)
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    mc.renderEngine.bindTexture(TEX_UTILS)
    drawTexturedModalRect((width * 2) - 133, 5, 0, 42, 128, 128)
    GL11.glScalef(2F, 2F, 2F)
    if (mc.thePlayer.worldObj.isRemote && (colorData != null) && (posData != null) && (colorData.contains(mc.thePlayer)) && (posData.contains(mc.thePlayer)) && (colorData(mc.thePlayer) != null) && (posData(mc.thePlayer) != null)) {
      for (i <- 0 until colorData(mc.thePlayer).length) {
        val colorIndex = colorData(mc.thePlayer)(i)
        if ((colorIndex / 4) == 0) rgbColors(i) = ((((i + (i / 128)) & 1) * 8) + 16) << 24
        else if (colorIndex >= 0) {
          val colorValue = MapColor.COLORS(colorIndex / 4).colorValue
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
        if (mc.thePlayer == player) mc.renderEngine.bindTexture(TEX_RED_DOT)
        else mc.renderEngine.bindTexture(TEX_BLUE_DOT)

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