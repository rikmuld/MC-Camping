package com.rikmuld.camping.client.render.objs

import net.minecraftforge.client.IItemRenderer.ItemRenderType
import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import org.lwjgl.opengl.GL11
import net.minecraftforge.client.IItemRenderer
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.common.objs.tile.TileEntityLog
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import org.lwjgl.opengl.GL12
import net.minecraft.tileentity.TileEntity

class LogRender extends TileEntitySpecialRenderer {
  override def renderTileEntityAt(tileentity: TileEntity, x: Double, y: Double, z: Double, fl: Float) {
    GL11.glPushMatrix()
    val model = Objs.logM
    GL11.glTranslatef(x.toFloat + 0.5F, y.toFloat + 0.03125F, z.toFloat + 0.5F)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)
    val log = tileentity.asInstanceOf[TileEntityLog]
    val world = log.getWorldObj
    val xPos = log.xCoord
    val yPos = log.yCoord
    val zPos = log.zCoord
    val meta = (log.rotation + 1) % 2
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    if (meta == 0) {
      bindTexture(new ResourceLocation(TextureInfo.MODEL_LOG))
      model.renderPart("Base")
      model.renderPart("BaseBack")
      model.renderPart("BaseFront")
      if (world.getBlock(xPos, yPos, zPos - 1) == Objs.log && (((world.getTileEntity(xPos, yPos, zPos - 1).asInstanceOf[TileEntityLog].rotation + 1) % 2) == 0)) {
        model.renderPart("ExtentionBack")
      }
      if (world.getBlock(xPos, yPos, zPos + 1) == Objs.log && (((world.getTileEntity(xPos, yPos, zPos + 1).asInstanceOf[TileEntityLog].rotation + 1) % 2) == 0)) {
        model.renderPart("ExtentionFront")
      }
    } else if (meta == 1) {
      bindTexture(new ResourceLocation(TextureInfo.MODEL_LOG2))
      model.renderPart("Base")
      model.renderPart("BaseLeft")
      model.renderPart("BaseRight")
      if (world.getBlock(xPos - 1, yPos, zPos) == Objs.log && (((world.getTileEntity(xPos - 1, yPos, zPos).asInstanceOf[TileEntityLog].rotation + 1) % 2) == 1)) {
        model.renderPart("ExtentionRight")
      }
      if (world.getBlock(xPos + 1, yPos, zPos) == Objs.log && (((world.getTileEntity(xPos + 1, yPos, zPos).asInstanceOf[TileEntityLog].rotation + 1) % 2) == 1)) {
        model.renderPart("ExtentionLeft")
      }
    }
    GL11.glPopMatrix()
  }
}

class LogItemRender extends IItemRenderer {
  override def handleRenderType(item: ItemStack, `type`: ItemRenderType): Boolean = true
  override def renderItem(`type`: ItemRenderType, item: ItemStack, data: AnyRef*) {
    GL11.glPushMatrix()
    if (`type` != ItemRenderType.ENTITY) GL11.glTranslatef(0, -0.35F, 0)
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
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_LOG))
    Objs.logM.renderPart("Base")
    Objs.logM.renderPart("BaseBack")
    Objs.logM.renderPart("BaseFront")
    GL11.glPopMatrix()
  }
  override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean = true
}
