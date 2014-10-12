package com.rikmuld.camping.client.render.objs

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import org.lwjgl.opengl.GL12
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import java.util.Random
import com.rikmuld.camping.core.Objs
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import com.rikmuld.camping.common.objs.tile.TileEntityCampfire

class CampfireRender extends TileEntitySpecialRenderer {
  var renderer: ItemRenderer = new ItemRenderer(Minecraft.getMinecraft)
  var rand: Random = new Random()

  override def renderTileEntityAt(tileentity: TileEntity, x: Double, y: Double, z: Double, f: Float) {
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    val tile = tileentity.asInstanceOf[TileEntityCampfire]
    bindTexture(new ResourceLocation(TextureInfo.MODEL_CAMPFIRE))
    GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat + 0.03125F, z.toFloat + 0.5F)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)
    Objs.campfireM.renderAll()
    GL11.glPopMatrix()
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glTranslatef(x.toFloat + 0.4F, y.toFloat + 0.0625f, z.toFloat + 0.4F)
    val coalItem = new ItemStack(Items.coal, 1, 0)
    for (i <- 0 until 20) {
      GL11.glPushMatrix()
      GL11.glTranslatef(tile.coals(0)(i), 0, tile.coals(1)(i))
      GL11.glScalef(0.2F, 0.15F, 0.15F)
      GL11.glRotatef(tile.coals(2)(i), 0, 1, 0)
      GL11.glRotatef(45, 1F, 1F, 0.4F)
      GL11.glRotatef(10, 0.0F, 1F, 0F)
      GL11.glRotatef(5, 0.0F, 0F, -0.2F)
      renderer.renderItem(tileentity.getWorldObj.getClosestPlayer(x, y, z, -1), coalItem, 0)
      GL11.glPopMatrix()
    }
    GL11.glPopMatrix()
  }
}

class CampfireItemRender extends IItemRenderer {
  override def handleRenderType(item: ItemStack, `type`: ItemRenderType): Boolean = true
  override def renderItem(`type`: ItemRenderType, item: ItemStack, data: AnyRef*) {
    GL11.glPushMatrix()
    if (`type` != ItemRenderType.ENTITY) GL11.glTranslatef(0, -0.35F, 0)
    if (`type` == ItemRenderType.EQUIPPED) GL11.glTranslatef(0.6F, 0.6F, 0.6F)
    if (`type` == ItemRenderType.EQUIPPED_FIRST_PERSON) GL11.glTranslatef(0, 1F, 0.7F)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)
    GL11.glScalef(1.4F, 1.4F, 1.4F)
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_CAMPFIRE))
    Objs.campfireM.renderAll()
    GL11.glPopMatrix()
  }
  override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean = true
}
