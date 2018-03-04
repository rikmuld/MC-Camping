package com.rikmuld.camping.render.objs

import com.rikmuld.camping.Library._
import com.rikmuld.camping.render.models.TentModel
import com.rikmuld.camping.tileentity.TileTent
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.{GL11, GL12}

object TentRender {
  final val MODEL =
    TentModel.TENT_MODEL

  final val TEX =
    new ResourceLocation(TextureInfo.MODEL_TENT_WHITE)

  def setTentColor(color: Int): Unit = color match {
    case 0 => GL11.glColor3f(.2f, .2f, .2f)
    case 1 => GL11.glColor3f(.67f, .16f, .16f)
    case 2 => GL11.glColor3f(.2f, .27f, .08f)
    case 3 => GL11.glColor3f(.3f, .2f, .1f)
    case 4 => GL11.glColor3f(.16f, .2f, .63f)
    case 5 => GL11.glColor3f(.55f, .24f, .7f)
    case 6 => GL11.glColor3f(.16f, .45f, .59f)
    case 7 => GL11.glColor3f(.63f, .63f, .63f)
    case 8 => GL11.glColor3f(.35f, .35f, .35f)
    case 9 => GL11.glColor3f(.86f, .5f, .6f)
    case 10 => GL11.glColor3f(.6f, .75f, .17f)
    case 11 => GL11.glColor3f(.78f, .75f, .12f)
    case 12 => GL11.glColor3f(.31f, .51f, .70f)
    case 13 => GL11.glColor3f(.75f, .18f, .75f)
    case 14 => GL11.glColor3f(.71f, .43f, .16f)
    case 15 => GL11.glColor3f(1, 1, 1)
  }
}

class TentRender extends TileEntitySpecialRenderer[TileTent] {

  override def render(tile: TileTent, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float): Unit = {
    GL11.glPushMatrix()
    GL11.glTranslatef(x.toFloat + 0.5f, y.toFloat + 1.5f, z.toFloat + 0.5f)
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)//TODO set as scaling of model itself
    GL11.glRotatef(tile.getFacing.rotateY.getHorizontalAngle, 0, 1, 0)
    bindTexture(new ResourceLocation(TextureInfo.MODEL_TENT_WHITE))
    TentRender.setTentColor(tile.color)
    TentRender.MODEL.renderOnly(TentModel.CANVAS:_*)
    GL11.glColor3f(1, 1, 1)
    TentRender.MODEL.renderOnly(TentModel.PEGS:_*)
//    TentRender.MODEL.renderOnly(TentModel.getPartsFor(tile.chests, tile.beds > 0):_*)
    GL11.glPopMatrix()
//
//    if (tile.lanterns > 0) {
//      GL11.glTranslatef(0, -1.5f, .03f)
//      val lanternStack = new ItemStack(Objs.lantern, 1, tile.lanternDamage)
//      if ((tile.getRotation == 0) || (tile.getRotation == 2)) GL11.glRotatef(90, 0F, 1F, 0F)
//      if ((tile.getRotation == 0) || (tile.getRotation == 1)) GL11.glTranslatef(-1, 1.2f, -.02f)
//      if ((tile.getRotation == 2) || (tile.getRotation == 3)) GL11.glTranslatef(1, 1.2f, -.02F)
//      GL11.glRotatef(180, 1, 0, 0)
//      GL11.glScalef(0.4F, -0.4F, -0.4f)
//      if (tile.getWorld.getClosestPlayer(x, y, z, -1, false) != null && lanternStack != null) renderer.renderItem(tile.getWorld.getClosestPlayer(x, y, z, -1, false), lanternStack, TransformType.NONE)
//    }
    GL11.glPopMatrix()
  }
}