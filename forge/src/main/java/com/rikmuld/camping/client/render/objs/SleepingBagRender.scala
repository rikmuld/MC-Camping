package com.rikmuld.camping.client.render.objs

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.ResourceLocation
import com.rikmuld.camping.core.TextureInfo
import net.minecraft.tileentity.TileEntity
import com.rikmuld.camping.common.objs.tile.TileEntitySleepingBag
import com.rikmuld.camping.misc.AbstractBox
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import net.minecraft.client.renderer.Tessellator

class SleepingBagRender extends TileEntitySpecialRenderer {
  var bed: AbstractBox = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 16, 1, 16, 0.0625F, 0.0F, 0.0F, 0.0F)

  override def renderTileEntityAt(tileentity: TileEntity, x: Double, y: Double, z: Double, f: Float) {
    val tile = tileentity.asInstanceOf[TileEntitySleepingBag]
    GL11.glPushMatrix()
    GL11.glEnable(GL12.GL_RESCALE_NORMAL)
    GL11.glTranslatef(x.toFloat, y.toFloat + 0.0625F, z.toFloat + 1F)
    GL11.glScalef(1.0F, -1F, -1F)
    GL11.glRotatef(90 * tile.rotation, 0, 1, 0)
    tile.rotation match {
      case 0 => ;
      case 1 => GL11.glTranslatef(-1, 0, 0)
      case 2 => GL11.glTranslatef(-1, 0, -1)
      case 3 => GL11.glTranslatef(0, 0, -1)
    }
    if (tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord) == 0) {
      bindTexture(new ResourceLocation(TextureInfo.MODEL_SLEEPING_TOP))
    } else {
      bindTexture(new ResourceLocation(TextureInfo.MODEL_SLEEPING_DOWN))
    }
    bed.render(Tessellator.instance)
    GL11.glPopMatrix()
  }
}
