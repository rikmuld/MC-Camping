package com.rikmuld.camping.features.entities.camper

import com.rikmuld.camping.Library._
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.entity.{RenderLiving, RenderManager}
import net.minecraft.util._
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
class RendererCamper(manager: RenderManager) extends RenderLiving[EntityCamper](manager, new ModelBiped(), 0.5f) {
  override def doRender(entity: EntityCamper, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    super.doRender(entity, d0, d1, d2, f, f1)
  }
  protected override def getEntityTexture(entity: EntityCamper): ResourceLocation = new ResourceLocation(if (entity.asInstanceOf[EntityCamper].getGender == 0) TextureInfo.MODEL_CAMPER_MALE else TextureInfo.MODEL_CAMPER_FEMALE)
}