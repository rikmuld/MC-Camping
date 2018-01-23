package com.rikmuld.camping.render.models

import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.util.math.MathHelper
import org.lwjgl.opengl.GL11

class FoxModel extends ModelBase {
  val f = 0.0F
  val f1 = 13.5F

  val wolfHeadMain = new ModelRenderer(this, 0, 0)
  val wolfBody = new ModelRenderer(this, 18, 14)
  val wolfLeg1 = new ModelRenderer(this, 0, 18)
  val wolfLeg2 = new ModelRenderer(this, 0, 18)
  val wolfLeg3 = new ModelRenderer(this, 0, 18)
  val wolfLeg4 = new ModelRenderer(this, 0, 18)
  val wolfTail = new ModelRenderer(this, 9, 18)
  val wolfMane = new ModelRenderer(this, 21, 0)

  this.wolfHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, f)
  this.wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F)
  this.wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f)
  this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F)
  this.wolfMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, f)
  this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F)
  this.wolfLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f)
  this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F)
  this.wolfLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f)
  this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F)
  this.wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f)
  this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F)
  this.wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f)
  this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F)
  this.wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f)
  this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F)
  this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, f)
  this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, f)
  this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, f)

  override def render(entity: Entity, f2: Float, f3: Float, f4: Float, f5: Float, f6: Float, f7: Float) {
    super.render(entity, f2, f3, f4, f5, f6, f7)
    this.setRotationAngles(f2, f3, f4, f5, f6, f7, entity)

    GL11.glPushMatrix
    GL11.glTranslatef(0, 0.6f, 0)
    GL11.glScalef(0.6f, 0.6f, 0.9f)
    if (this.isChild) {
      val f6 = 2.0F
      GL11.glTranslatef(0, -1.2f, 0)
      GL11.glPushMatrix
      GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6)
      GL11.glTranslatef(0.0F, 24.0F * f7, 0.0F)
      this.wolfHeadMain.renderWithRotation(f7)
      this.wolfBody.render(f7)
      this.wolfLeg1.render(f7)
      this.wolfLeg2.render(f7)
      this.wolfLeg3.render(f7)
      this.wolfLeg4.render(f7)
      this.wolfTail.renderWithRotation(f7)
      this.wolfMane.render(f7)
      GL11.glPopMatrix
    } else {
      this.wolfHeadMain.renderWithRotation(f7)
      this.wolfBody.render(f7)
      this.wolfLeg1.render(f7)
      this.wolfLeg2.render(f7)
      this.wolfLeg3.render(f7)
      this.wolfLeg4.render(f7)
      this.wolfTail.renderWithRotation(f7)
      this.wolfMane.render(f7)
    }
    GL11.glPopMatrix
  }
  override def setLivingAnimations(entity: EntityLivingBase, f2: Float, f3: Float, f4: Float) {
    this.wolfTail.rotateAngleY = MathHelper.cos(f2 * 0.6662F) * 1.4F * f3
    this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F)
    this.wolfBody.rotateAngleX = (Math.PI.asInstanceOf[Float] / 2F)
    this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F)
    this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX
    this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F)
    this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F)
    this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F)
    this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F)
    this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F)
    this.wolfLeg1.rotateAngleX = MathHelper.cos(f2 * 0.6662F) * 1.4F * f3
    this.wolfLeg2.rotateAngleX = MathHelper.cos(f2 * 0.6662F + Math.PI.asInstanceOf[Float]) * 1.4F * f3
    this.wolfLeg3.rotateAngleX = MathHelper.cos(f2 * 0.6662F + Math.PI.asInstanceOf[Float]) * 1.4F * f3
    this.wolfLeg4.rotateAngleX = MathHelper.cos(f2 * 0.6662F) * 1.4F * f3
  }
  override def setRotationAngles(f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, f6: Float, entity: Entity) {
    super.setRotationAngles(f1, f2, f3, f4, f5, f6, entity)
    this.wolfHeadMain.rotateAngleX = f5 / (180F / Math.PI.asInstanceOf[Float])
    this.wolfHeadMain.rotateAngleY = f4 / (180F / Math.PI.asInstanceOf[Float])
    this.wolfTail.rotateAngleX = (Math.PI / 3f).asInstanceOf[Float]
  }
}