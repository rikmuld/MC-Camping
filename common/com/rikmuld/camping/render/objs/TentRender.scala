package com.rikmuld.camping.render.objs

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import com.rikmuld.camping.objs.Objs
import net.minecraft.util.ResourceLocation
import net.minecraft.tileentity.TileEntity
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import org.lwjgl.opengl.GL12
import net.minecraft.item.ItemStack
import com.rikmuld.camping.render.models.TentModel
import com.rikmuld.camping.Lib._
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import com.rikmuld.camping.objs.tile.TileTent

object TentRender {
  final val MODEL = new TentModel
  final val TEX = new ResourceLocation(TextureInfo.MODEL_TENT_WHITE)
  
  def setTentColor(color: Int) {
    if (color == 0) GL11.glColor3f(.2f, .2f, .2f)
    if (color == 1) GL11.glColor3f(.67f, .16f, .16f)
    if (color == 2) GL11.glColor3f(.2f, .27f, .08f)
    if (color == 3) GL11.glColor3f(.3f, .2f, .1f)
    if (color == 4) GL11.glColor3f(.16f, .2f, .63f)
    if (color == 5) GL11.glColor3f(.55f, .24f, .7f)
    if (color == 6) GL11.glColor3f(.16f, .45f, .59f)
    if (color == 7) GL11.glColor3f(.63f, .63f, .63f)
    if (color == 8) GL11.glColor3f(.35f, .35f, .35f)
    if (color == 9) GL11.glColor3f(.86f, .5f, .6f)
    if (color == 10) GL11.glColor3f(.6f, .75f, .17f)
    if (color == 11) GL11.glColor3f(.78f, .75f, .12f)
    if (color == 12) GL11.glColor3f(.31f, .51f, .70f)
    if (color == 13) GL11.glColor3f(.75f, .18f, .75f)
    if (color == 14) GL11.glColor3f(.71f, .43f, .16f)
    if (color == 15) GL11.glColor3f(1, 1, 1)
  }
}

class TentRender extends TileEntitySpecialRenderer[TileTent] {
  var renderer = Minecraft.getMinecraft.getItemRenderer

  override def renderTileEntityAt(tile: TileTent, x: Double, y: Double, z: Double, f: Float, i:Int) {
    GL11.glPushMatrix()
    GL11.glTranslatef(x.toFloat + 0.5f, y.toFloat + 1.5f, z.toFloat + 0.5f)
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)
    GL11.glRotatef(if ((tile.getRotation + 1) > 3) 90 * 0 else 90 * (tile.getRotation + 1), 0, 1, 0)
    bindTexture(new ResourceLocation(TextureInfo.MODEL_TENT_WHITE))
    TentRender.setTentColor(tile.color)
    TentRender.MODEL.renderOnly(TentModel.CANVAS:_*)
    GL11.glColor3f(1, 1, 1)
    TentRender.MODEL.renderOnly(TentModel.PEGS:_*)
    TentRender.MODEL.renderOnly(TentModel.getPartsFor(tile.chests, tile.beds > 0):_*)
    GL11.glPopMatrix()
    
    if (tile.lanterns > 0) {
      GL11.glTranslatef(0, -1.5f, .03f)
      val lanternStack = new ItemStack(Objs.lantern, 1, tile.lanternDamage)
      if ((tile.getRotation == 0) || (tile.getRotation == 2)) GL11.glRotatef(90, 0F, 1F, 0F)
      if ((tile.getRotation == 0) || (tile.getRotation == 1)) GL11.glTranslatef(-1, 1.2f, -.02f)
      if ((tile.getRotation == 2) || (tile.getRotation == 3)) GL11.glTranslatef(1, 1.2f, -.02F)
      GL11.glRotatef(180, 1, 0, 0)
      GL11.glScalef(0.4F, -0.4F, -0.4f)
      if (tile.getWorld.getClosestPlayer(x, y, z, -1, false) != null && lanternStack != null) renderer.renderItem(tile.getWorld.getClosestPlayer(x, y, z, -1, false), lanternStack, TransformType.NONE)
    }
    GL11.glPopMatrix()
  }
}