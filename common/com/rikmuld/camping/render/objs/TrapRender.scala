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
import com.rikmuld.camping.objs.tile.TileTrap
import com.rikmuld.camping.render.models.ModelTrapOpen
import com.rikmuld.camping.Lib._
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import com.rikmuld.camping.render.models.ModelTrapClosed
import net.minecraft.entity.Entity

object TrapRender {
  final val OPEN = new ModelTrapOpen
  final val CLOSED = new ModelTrapClosed
  final val TEX_ALL = new ResourceLocation(TextureInfo.MODEL_TRAP)
}

class TrapRender extends TileEntitySpecialRenderer[TileTrap] {
  var renderer: ItemRenderer = new ItemRenderer(Minecraft.getMinecraft)
  override def renderTileEntityAt(tileentity: TileTrap, x: Double, y: Double, z: Double, f: Float, i:Int) {
    val tile = tileentity.asInstanceOf[TileTrap]
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glTranslatef(x.toFloat + 0.5f, y.toFloat + 1.5f, z.toFloat + 0.5f)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glScalef(0.0625F, 0.0625F, 0.0625F)
    bindTexture(TrapRender.TEX_ALL)
    if (tile.open) TrapRender.OPEN.renderAll
    else TrapRender.CLOSED.renderAll
    GL11.glPopMatrix()
    if (Option(tile.getStackInSlot(0)).isDefined) {
      GL11.glPushMatrix()
      GL11.glEnable(GL12.GL_RESCALE_NORMAL)
      GL11.glTranslatef((x + 0.5 + 0.03125F).toFloat, y.toFloat + 0.03125F, ((z + 0.51) - 0.0625F/2).toFloat)
      GL11.glRotatef(90, 0, 0, 1)
      GL11.glRotatef(180, 1, 0, 0)
      GL11.glScalef(0.2F, -0.2F, -0.2F)
      renderer.renderItem(tileentity.getWorld.getClosestPlayer(x, y, z, -1, false), tile.getStackInSlot(0), TransformType.FIRST_PERSON_LEFT_HAND)
      GL11.glPopMatrix()
    }
  }
}