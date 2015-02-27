package com.rikmuld.camping.client.render.objs

import org.lwjgl.opengl.GL11

import com.rikmuld.camping.core.TextureInfo

import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.EntityLiving
import net.minecraft.util.ResourceLocation

@SideOnly(Side.CLIENT)
class FoxRenderer(model: ModelBase) extends RenderLiving(model, 0.4f) {
  override def doRender(entity: Entity, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    GL11.glPushMatrix()
    if (entity.asInstanceOf[EntityAgeable].isChild) GL11.glTranslatef(0, -0.75F, 0)
    super.doRender(entity.asInstanceOf[EntityLiving], d0, d1, d2, f, f1)
    GL11.glPopMatrix()
  }
  protected override def getEntityTexture(par1Entity: Entity): ResourceLocation = new ResourceLocation(TextureInfo.MODEL_FOX)
}