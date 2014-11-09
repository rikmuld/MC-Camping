package com.rikmuld.camping.client.render.objs

import com.rikmuld.camping.common.objs.entity.Camper
import com.rikmuld.camping.core.TextureInfo

import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.model.ModelBase
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.util.ResourceLocation

@SideOnly(Side.CLIENT)
class CamperRender(model: ModelBase) extends RenderLiving(model, 0.5f) {
  override def doRender(entity: Entity, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    super.doRender(entity.asInstanceOf[EntityLiving], d0, d1, d2, f, f1)
  }
  protected override def getEntityTexture(entity: Entity): ResourceLocation = new ResourceLocation(if(entity.asInstanceOf[Camper].getGender==0) TextureInfo.MODEL_CAMPER_MALE else TextureInfo.MODEL_CAMPER_FEMALE)
}