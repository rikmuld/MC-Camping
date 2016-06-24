package com.rikmuld.camping.render.models

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11
import com.rikmuld.corerm.client.RMModel
import com.rikmuld.camping.objs.entity.Bear
import net.minecraft.util.math.MathHelper

object BearModel {
  final val LEG_1 = "leg1"
  final val LEG_2 = "leg2"
  final val LEG_3 = "leg3"
  final val LEG_4 = "leg4"
}

class BearModel extends RMModel(128, 64) {
  addBox(BearModel.LEG_1, 0, 27, 6, 11, 6, 3F, 13F, -8F, 0F, 0F, 0F)
  addBox(BearModel.LEG_2, 0, 44, 6, 14, 6, 4F, 10F, 9F, 0F, 0F, 0F)
  addBox(BearModel.LEG_3, 0, 27, 6, 11, 6, -9F, 13F, -8F, 0F, 0F, 0F)
  addBox(BearModel.LEG_4, 0, 44, 6, 14, 6, -10F, 10F, 9F, 0F, 0F, 0F)
  addBox("earRight", 58, 38, 3, 3, 3, 4F, 5.5F, -14F, 0F, 0F, 0F)
  addBox("middleBack", 0, 0, 18, 14, 13, -9F, 3F, 3F, 0F, 0F, 0F)
  addBox("middleFront", 58, 0, 16, 12, 13, -8F, 4F, -10F, 0F, 0F, 0F)
  addBox("snout", 82, 27, 6, 5, 5, -3F, 10F, -22F, 0F, 0F, 0F)
  addBox("earLeft", 58, 37, 3, 3, 3, -7F, 5.5F, -14F, 0F, 0F, 0F)
  addBox("head", 24, 27, 12, 10, 7, -6F, 6F, -17F, 0F, 0F, 0F)
  setPersistentQueue(true, true)
  
  override def applyGl = if (isChild) GL11.glScalef(0.5F, 0.5F, 0.5F)
  override def setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, entity: Entity) {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
    getBox(BearModel.LEG_1).rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1
    getBox(BearModel.LEG_2).rotateAngleX = MathHelper.cos((f * 0.6662F) + Math.PI.toFloat) * 1.4F * f1
    getBox(BearModel.LEG_3).rotateAngleX = MathHelper.cos((f * 0.6662F) + Math.PI.toFloat) * 1.4F * f1
    getBox(BearModel.LEG_4).rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1
  }
}