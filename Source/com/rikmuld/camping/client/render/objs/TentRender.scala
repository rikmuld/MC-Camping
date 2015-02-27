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
  def setTentColor(color: Int) {
    if (color == 0) GL11.glColor3f(.2f, .2f, .2f);
    if (color == 1) GL11.glColor3f(.67f, .16f, .16f);
    if (color == 2) GL11.glColor3f(.2f, .27f, .08f);
    if (color == 3) GL11.glColor3f(.3f, .2f, .1f);
    if (color == 4) GL11.glColor3f(.16f, .2f, .63f);
    if (color == 5) GL11.glColor3f(.55f, .24f, .7f);
    if (color == 6) GL11.glColor3f(.16f, .45f, .59f);
    if (color == 7) GL11.glColor3f(.63f, .63f, .63f);
    if (color == 8) GL11.glColor3f(.35f, .35f, .35f);
    if (color == 9) GL11.glColor3f(.86f, .5f, .6f);
    if (color == 10) GL11.glColor3f(.6f, .75f, .17f);
    if (color == 11) GL11.glColor3f(.78f, .75f, .12f);
    if (color == 12) GL11.glColor3f(.31f, .51f, .70f);
    if (color == 13) GL11.glColor3f(.75f, .18f, .75f);
    if (color == 14) GL11.glColor3f(.71f, .43f, .16f);
    if (color == 15) GL11.glColor3f(1, 1, 1);
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
    bindTexture(new ResourceLocation(TextureInfo.MODEL_TENT_WHITE))
    TentRender.setTentColor(tile.color);
    Objs.tentM.renderAllExcept("bed1", "bed2", "chest1", "chest2", "chest3", "chest4", "chest5", "chest6", "chest7", "shape1", "shape2", "shape3", "shape4")
    GL11.glColor3f(1, 1, 1)
    Objs.tentM.renderOnly("shape1", "shape2", "shape3", "shape4")

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
      if (tileentity.getWorldObj().getClosestPlayer(x, y, z, -1) != null && lanternStack != null) renderer.renderItem(tileentity.getWorldObj().getClosestPlayer(x, y, z, -1), lanternStack, 0)
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
    } else if (`type` == ItemRenderType.EQUIPPED_FIRST_PERSON) {
      GL11.glRotatef(45, 0, 1, 0)
      GL11.glTranslatef(0.0F, 0.3F, 0.6F)
    }
    GL11.glScalef(0.02625F, -0.02625F, -0.02625F)
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_TENT_WHITE))
    TentRender.setTentColor(if (item.hasTagCompound()) item.getTagCompound.getInteger("color") else 15);
    Objs.tentM.renderAllExcept("bed1", "bed2", "chest1", "chest2", "chest3", "chest4", "chest5", "chest6", "chest7", "shape1", "shape2", "shape3", "shape4")
    GL11.glColor3f(1, 1, 1)
    Objs.tentM.renderOnly("shape1", "shape2", "shape3", "shape4")
    GL11.glPopMatrix()
  }
  override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean = true
}