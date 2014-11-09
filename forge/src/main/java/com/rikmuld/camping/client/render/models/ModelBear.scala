package com.rikmuld.camping.client.render.models

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11

class ModelBear extends ModelBase {
  textureWidth = 128
  textureHeight = 64

  var leg1: ModelRenderer = new ModelRenderer(this, 0, 27)
  leg1.addBox(0F, 0F, 0F, 6, 11, 6)
  leg1.setRotationPoint(3F, 13F, -8F)
  leg1.setTextureSize(128, 64)
  leg1.mirror = true
  setRotation(leg1, 0F, 0F, 0F)

  var leg3: ModelRenderer = new ModelRenderer(this, 0, 44)
  leg3.addBox(0F, 0F, 0F, 6, 14, 6)
  leg3.setRotationPoint(4F, 10F, 9F)
  leg3.setTextureSize(128, 64)
  leg3.mirror = true
  setRotation(leg3, 0F, 0F, 0F)

  var leg2: ModelRenderer = new ModelRenderer(this, 0, 27)
  leg2.addBox(0F, 0F, 0F, 6, 11, 6)
  leg2.setRotationPoint(-9F, 13F, -8F)
  leg2.setTextureSize(128, 64)
  leg2.mirror = true
  setRotation(leg2, 0F, 0F, 0F)

  var leg4: ModelRenderer = new ModelRenderer(this, 0, 44)
  leg4.addBox(0F, 0F, 0F, 6, 14, 6)
  leg4.setRotationPoint(-10F, 10F, 9F)
  leg4.setTextureSize(128, 64)
  leg4.mirror = true
  setRotation(leg4, 0F, 0F, 0F)

  var earRight: ModelRenderer = new ModelRenderer(this, 58, 38)
  earRight.addBox(0F, 0F, 0F, 3, 3, 3)
  earRight.setRotationPoint(4F, 5.5F, -14F)
  earRight.setTextureSize(128, 64)
  earRight.mirror = true
  setRotation(earRight, 0F, 0F, 0F)

  var middleBack: ModelRenderer = new ModelRenderer(this, 0, 0)
  middleBack.addBox(0F, 0F, 0F, 18, 14, 13)
  middleBack.setRotationPoint(-9F, 3F, 3F)
  middleBack.setTextureSize(128, 64)
  middleBack.mirror = true
  setRotation(middleBack, 0F, 0F, 0F)

  var middleFront: ModelRenderer = new ModelRenderer(this, 58, 0)
  middleFront.addBox(0F, 0F, 0F, 16, 12, 13)
  middleFront.setRotationPoint(-8F, 4F, -10F)
  middleFront.setTextureSize(128, 64)
  middleFront.mirror = true
  setRotation(middleFront, 0F, 0F, 0F)

  var snout: ModelRenderer = new ModelRenderer(this, 82, 27)
  snout.addBox(0F, 0F, 0F, 6, 5, 5)
  snout.setRotationPoint(-3F, 10F, -22F)
  snout.setTextureSize(128, 64)
  snout.mirror = true
  setRotation(snout, 0F, 0F, 0F)

  var earLeft: ModelRenderer = new ModelRenderer(this, 58, 37)
  earLeft.addBox(0F, 0F, 0F, 3, 3, 3)
  earLeft.setRotationPoint(-7F, 5.5F, -14F)
  earLeft.setTextureSize(128, 64)
  earLeft.mirror = true
  setRotation(earLeft, 0F, 0F, 0F)

  var head: ModelRenderer = new ModelRenderer(this, 24, 27)
  head.addBox(0F, 0F, 0F, 12, 10, 7)
  head.setRotationPoint(-6F, 6F, -17F)
  head.setTextureSize(128, 64)
  head.mirror = true
  setRotation(head, 0F, 0F, 0F)

  override def render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
    super.render(entity, f, f1, f2, f3, f4, f5)
    setRotationAngles(entity, f, f1, f2, f3, f4, f5)
    if (isChild) GL11.glScalef(0.5F, 0.5F, 0.5F)
    leg1.render(f5)
    leg3.render(f5)
    leg2.render(f5)
    leg4.render(f5)
    earRight.render(f5)
    middleBack.render(f5)
    middleFront.render(f5)
    snout.render(f5)
    earLeft.render(f5)
    head.render(f5)
  }
  private def setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
    model.rotateAngleX = x
    model.rotateAngleY = y
    model.rotateAngleZ = z
  }
  def setRotationAngles(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
    leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1
    leg2.rotateAngleX = MathHelper.cos((f * 0.6662F) + Math.PI.toFloat) * 1.4F * f1
    leg3.rotateAngleX = MathHelper.cos((f * 0.6662F) + Math.PI.toFloat) * 1.4F * f1
    leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1
  }
}
