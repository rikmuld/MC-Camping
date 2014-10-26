package com.rikmuld.camping.client.render.objs

import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper
import com.rikmuld.camping.core.Objs
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.lwjgl.opengl.GL12
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.tileentity.TileEntity
import com.rikmuld.camping.common.objs.tile.TileEntityTrap

class TrapRender extends TileEntitySpecialRenderer{
  var renderer: ItemRenderer = new ItemRenderer(Minecraft.getMinecraft)
  override def renderTileEntityAt(tileentity: TileEntity, x: Double, y: Double, z: Double, f: Float) {
    val tile = tileentity.asInstanceOf[TileEntityTrap]
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat + 0.03125F, z.toFloat + 0.5F)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)
    bindTexture(new ResourceLocation(TextureInfo.MODEL_TRAP))
    if (tile.open) Objs.trapOpen.renderAll()
    else Objs.trapClose.renderAll()
    GL11.glPopMatrix()
    if (tile.getStackInSlot(0) != null) {
      GL11.glPushMatrix()
      GL11.glEnable(GL12.GL_RESCALE_NORMAL)
      GL11.glTranslatef((x + 0.5 + 0.03125F).toFloat, y.toFloat + 0.03125F, ((z + 0.5) - 0.0625F).toFloat)
      GL11.glRotatef(90, 0, 0, 1)
      GL11.glRotatef(180, 1, 0, 0)
      GL11.glRotatef(-41, 0, 1, 0)
      GL11.glRotatef(-25, -1, 0, 1)
      GL11.glScalef(0.1F, -0.1F, -0.1F)
      renderer.renderItem(tileentity.getWorldObj().getClosestPlayer(x, y, z, -1), tile.getStackInSlot(0), 0)
      GL11.glPopMatrix()
    }
  }
}

class TrapItemRenderer extends IItemRenderer {
  override def handleRenderType(item: ItemStack, `type`: ItemRenderType): Boolean = true
  override def renderItem(`type`: ItemRenderType, item: ItemStack, data: AnyRef*) {
    GL11.glPushMatrix()
    if (`type` != ItemRenderType.ENTITY) {
      GL11.glTranslatef(0, -0.35F, 0)
    }
    if (`type` == ItemRenderType.INVENTORY) {
      GL11.glTranslatef(0, 0.2F, 0)
      GL11.glScalef(1.1F, 1.1F, 1.1F)
    }
    if (`type` == ItemRenderType.EQUIPPED) {
      GL11.glRotatef(45, 0, 1, 0)
      GL11.glRotatef(35, 1, 0, 0)
      GL11.glTranslatef(0.0F, 0.85F, 0.05F)
    }
    if (`type` == ItemRenderType.EQUIPPED_FIRST_PERSON) {
      GL11.glRotatef(45, 0, -1, 0)
      GL11.glTranslatef(0.4F, 1F, 0.7F)
    }
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)
    GL11.glScalef(1.4F, 1.4F, 1.4F)
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_TRAP))
    Objs.trapOpen.renderAll()
    GL11.glPopMatrix()
  }
  override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean = true
}