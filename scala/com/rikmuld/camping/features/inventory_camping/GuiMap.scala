package com.rikmuld.camping.features.inventory_camping

import java.awt.Color

import com.rikmuld.camping.Library._
import com.rikmuld.camping.features.inventory_camping.GuiMap.MapData
import net.minecraft.block.material.MapColor
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, GL12}

object GuiMap {
  final val TEX_MAP: Int =
    GL11.glGenTextures()

  final val TEX_UTILS =
    new ResourceLocation(TextureInfo.GUI_UTILS)

  final val TEX_RED_DOT =
    new ResourceLocation(TextureInfo.RED_DOT)

  final val TEX_BLUE_DOT =
    new ResourceLocation(TextureInfo.BLUE_DOT)

  def initTexture(colors: Array[Byte]): Unit = {
    val rgbColors: Seq[Int] =
      for (i <- colors.indices)
        yield colorIndexToColor(i, colors(i))

    val textureBuffer = BufferUtils.createByteBuffer(128 * 128 * 3)

    for (i <- 0 until (128 * 128)) {
      val pixel = rgbColors(i)
      val color = Color.decode(java.lang.Integer.toString(pixel))
      textureBuffer.put(color.getRed.toByte)
      textureBuffer.put(color.getGreen.toByte)
      textureBuffer.put(color.getBlue.toByte)
    }

    textureBuffer.flip()
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, TEX_MAP)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 128, 128, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, textureBuffer)
  }

  def colorIndexToColor(i: Int, colorIndex: Byte): Int =
    if (((colorIndex & 255) / 4) == 0)
      ((((i + (i / 128)) & 1) * 8) + 16) << 24
    else
      MapColor.COLORS((colorIndex & 255) / 4).getMapColor(colorIndex & 3)

  case class MapData(xCenter: Int, zCenter: Int, scale: Int, colors: Array[Byte])
}

class GuiMap(mapData: MapData) extends Gui {
  GuiMap.initTexture(mapData.colors)

  def drawMap(mc: Minecraft, width: Int, height: Int):Unit = {
    GL11.glPushMatrix()
    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    GL11.glDisable(GL11.GL_LIGHTING)
    GL11.glScalef(0.5F, 0.5F, 0.5F)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

    mc.renderEngine.bindTexture(GuiMap.TEX_UTILS)

    drawTexturedModalRect((width * 2) - 133, 5, 0, 42, 128, 128)
    GL11.glScalef(2F, 2F, 2F)

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, GuiMap.TEX_MAP)

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

    for (i <- 0 until mc.world.playerEntities.size)
      drawPlayer(mc, width, height, mc.world.playerEntities.get(i))

    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    GL11.glPopMatrix()
  }

  def drawPlayer(mc: Minecraft, width: Int, height: Int, player: EntityPlayer): Unit = {
    val mapScale = (57F / (64F * Math.pow(2, mapData.scale))).toFloat
    var xDivision = (mapScale * (player.posX - mapData.xCenter)).toInt
    var zDivision = (mapScale * (player.posZ - mapData.zCenter)).toInt
    if (xDivision > 57) xDivision = 57
    else if (xDivision < -57) xDivision = -57
    if (zDivision > 57) zDivision = 57
    else if (zDivision < -57) zDivision = -57
    if (mc.player == player) mc.renderEngine.bindTexture(GuiMap.TEX_RED_DOT)
    else mc.renderEngine.bindTexture(GuiMap.TEX_BLUE_DOT)

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