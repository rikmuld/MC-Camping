package com.rikmuld.camping.client.render.objs

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import com.rikmuld.camping.common.objs.tile.TileEntityTent
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import org.lwjgl.opengl.GL12
import net.minecraft.item.ItemStack
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.TextureInfo
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType

object TentRender {
  def getTentTexture(color: Int): ResourceLocation = color match {
    case 0 => new ResourceLocation(TextureInfo.MODEL_TENT_BLACK)
    case 1 => new ResourceLocation(TextureInfo.MODEL_TENT_RED)
    case 2 => new ResourceLocation(TextureInfo.MODEL_TENT_GREEN)
    case 3 => new ResourceLocation(TextureInfo.MODEL_TENT_BROWN)
    case 4 => new ResourceLocation(TextureInfo.MODEL_TENT_BLUE)
    case 5 => new ResourceLocation(TextureInfo.MODEL_TENT_PURPLE)
    case 6 => new ResourceLocation(TextureInfo.MODEL_TENT_CYAN)
    case 7 => new ResourceLocation(TextureInfo.MODEL_TENT_LIGHTGRAY)
    case 8 => new ResourceLocation(TextureInfo.MODEL_TENT_GRAY)
    case 9 => new ResourceLocation(TextureInfo.MODEL_TENT_PINK)
    case 10 => new ResourceLocation(TextureInfo.MODEL_TENT_LIME)
    case 11 => new ResourceLocation(TextureInfo.MODEL_TENT_YELLOW)
    case 12 => new ResourceLocation(TextureInfo.MODEL_TENT_LIGHTBLUE)
    case 13 => new ResourceLocation(TextureInfo.MODEL_TENT_MAGENTA)
    case 14 => new ResourceLocation(TextureInfo.MODEL_TENT_ORANGE)
    case 15 => new ResourceLocation(TextureInfo.MODEL_TENT_WHITE)
    case _ => new ResourceLocation(TextureInfo.MODEL_TENT_WHITE)
  }
}

class TentRender extends TileEntitySpecialRenderer {
  var renderer: ItemRenderer = new ItemRenderer(Minecraft.getMinecraft)

  override def renderTileEntityAt(tileentity: TileEntity, x: Double, y: Double, z: Double, f: Float) {
    val tile = tileentity.asInstanceOf[TileEntityTent]
    GL11.glPushMatrix()
    GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat + 0.03125F, z.toFloat + 0.5F)
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)
    GL11.glRotatef(if ((tile.rotation + 1) > 3) 90 * 0 else 90 * (tile.rotation + 1), 0, 1, 0)
    bindTexture(TentRender.getTentTexture(tile.color))
    Objs.tentM.renderAllExcept("bed1", "bed2", "chest1", "chest2", "chest3", "chest4", "chest5", "chest6", "chest7")
    if (tile.beds > 0) {
      if (tile.chests == 0) Objs.tentM.renderPart("bed1")
      else Objs.tentM.renderPart("bed2")
    }
    if (tile.chests > 0) {
      if (tile.beds == 0) {
        if (tile.chests > 0) Objs.tentM.renderOnly("chest3")
        if (tile.chests > 1) Objs.tentM.renderOnly("chest4")
        if (tile.chests > 2) Objs.tentM.renderOnly("chest5")
        if (tile.chests > 3) Objs.tentM.renderOnly("chest6")
        if (tile.chests > 4) Objs.tentM.renderOnly("chest7")
      } else {
        if (tile.chests > 0) Objs.tentM.renderOnly("chest1")
        if (tile.chests > 1) Objs.tentM.renderOnly("chest2")
      }
    }
    GL11.glPopMatrix()
    if (tile.lanterns > 0) {
      val lanternStack = new ItemStack(Objs.lantern, 1, tile.lanternDamage)
      if ((tile.rotation == 0) || (tile.rotation == 2)) GL11.glRotatef(90, 0F, 1F, 0F)
      if ((tile.rotation == 0) || (tile.rotation == 1)) GL11.glTranslatef(-1, 0.9375F, -0.155F)
      if ((tile.rotation == 2) || (tile.rotation == 3)) GL11.glTranslatef(1, 0.9375F, -0.175F)
      GL11.glRotatef(180, 1, 0, 0)
      GL11.glRotatef(-40, 0, 1, 0)
      GL11.glRotatef(-25, -1, 0, 1)
      GL11.glScalef(0.3F, -0.3F, -0.3F)
      renderer.renderItem(tileentity.getWorldObj().getClosestPlayer(x, y, z, 2000), lanternStack, 0)
    }
    GL11.glPopMatrix()
  }
}

class TentItemRender extends IItemRenderer {
  override def handleRenderType(item: ItemStack, `type`: ItemRenderType): Boolean = true
  override def renderItem(`type`: ItemRenderType, item: ItemStack, data: AnyRef*) {
    GL11.glPushMatrix()
    GL11.glTranslatef(0, 0.1F, 0.45F)
    if (`type` == ItemRenderType.ENTITY) GL11.glTranslatef(-0.375F, 0, -0.5F)
    else if (`type` == ItemRenderType.EQUIPPED) {
      GL11.glRotatef(45, 0, -1, 0)
      GL11.glTranslatef(0.0F, 0.0F, -0.3F)
    }
    else if (`type` == ItemRenderType.EQUIPPED_FIRST_PERSON) {
      GL11.glRotatef(45, 0, 1, 0)
      GL11.glTranslatef(0.0F, 0.3F, 0.6F)
    }
    GL11.glScalef(0.02625F, -0.02625F, -0.02625F)
    Minecraft.getMinecraft.renderEngine.bindTexture(TentRender.getTentTexture(if (item.hasTagCompound()) item.getTagCompound.getInteger("color") else 15))
    Objs.tentM.renderAllExcept("bed1", "bed2", "chest1", "chest2", "chest3", "chest4", "chest5", "chest6", "chest7")
    GL11.glPopMatrix()
  }
  override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean = true
}