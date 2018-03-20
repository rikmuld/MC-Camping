package com.rikmuld.camping.features.entities.bear

import com.rikmuld.camping.Library._
import net.minecraft.client.renderer.entity.{RenderLiving, RenderManager}
import net.minecraft.entity._
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
class RendererBear(renderManager: RenderManager) extends RenderLiving[EntityBear](renderManager, new ModelBear(), 1) {
  override def doRender(entity: EntityBear, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    GL11.glPushMatrix()
    if (entity.asInstanceOf[EntityAgeable].isChild) GL11.glTranslatef(0, -0.75F, 0)
    super.doRender(entity, d0, d1, d2, f, f1)
    GL11.glPopMatrix()
  }
  protected override def getEntityTexture(par1Entity: EntityBear): ResourceLocation = new ResourceLocation(TextureInfo.MODEL_BEAR)
}