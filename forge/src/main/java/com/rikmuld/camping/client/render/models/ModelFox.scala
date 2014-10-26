package com.rikmuld.camping.client.render.models

import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11
import net.minecraft.entity.Entity

class ModelFox extends ModelBase {
  textureWidth = 64
  textureHeight = 32

  var leg1 = new ModelRenderer(this, 4, 27);
  leg1.addBox(0F, 0F, 0F, 1, 4, 1);
  leg1.setRotationPoint(-1.4F, 20F, -4.5F);
  leg1.setTextureSize(64, 32);
  leg1.mirror = true;
  setRotation(leg1, 0F, 0F, 0F);
  var leg2 = new ModelRenderer(this, 8, 27);
  leg2.addBox(0F, 0F, 0F, 1, 4, 1);
  leg2.setRotationPoint(0.4F, 20F, -4.5F);
  leg2.setTextureSize(64, 32);
  leg2.mirror = true;
  setRotation(leg2, 0F, 0F, 0F);
  var tail = new ModelRenderer(this, 30, 0);
  tail.addBox(0F, 0F, 0F, 1, 1, 6);
  tail.setRotationPoint(-0.5F, 18F, 4.5F);
  tail.setTextureSize(64, 32);
  tail.mirror = true;
  setRotation(tail, -0.5576851F, 0F, 0F);
  var leg3 = new ModelRenderer(this, 0, 27);
  leg3.addBox(0F, 0F, 0F, 1, 4, 1);
  leg3.setRotationPoint(-1.4F, 20F, 4.5F);
  leg3.setTextureSize(64, 32);
  leg3.mirror = true;
  setRotation(leg3, 0F, 0F, 0F);
  var ear1 = new ModelRenderer(this, 12, 25);
  ear1.addBox(0F, 0F, 0F, 1, 1, 1);
  ear1.setRotationPoint(-1.2F, 16.6F, -7F);
  ear1.setTextureSize(64, 32);
  ear1.mirror = true;
  setRotation(ear1, 0F, 0F, 0F);
  var leg4 = new ModelRenderer(this, 12, 27);
  leg4.addBox(0F, 0F, 0F, 1, 4, 1);
  leg4.setRotationPoint(0.4F, 20F, 4.5F);
  leg4.setTextureSize(64, 32);
  leg4.mirror = true;
  setRotation(leg4, 0F, 0F, 0F);
  var body = new ModelRenderer(this, 0, 0);
  body.addBox(0F, 0F, 0F, 3, 3, 12);
  body.setRotationPoint(-1.5F, 18F, -6F);
  body.setTextureSize(64, 32);
  body.mirror = true;
  setRotation(body, 0F, 0F, 0F);
  var head = new ModelRenderer(this, 0, 21);
  head.addBox(0F, 0F, 0F, 3, 3, 3);
  head.setRotationPoint(-1.5F, 17F, -8.2F);
  head.setTextureSize(64, 32);
  head.mirror = true;
  setRotation(head, 0F, 0F, 0F);
  var eye = new ModelRenderer(this, 0, 20);
  eye.addBox(0F, 0F, 0F, 1, 1, 0);
  eye.setRotationPoint(-1.2F, 17.7F, -8.2F);
  eye.setTextureSize(64, 32);
  eye.mirror = true;
  setRotation(eye, 0F, 0F, 0F);
  var ear2 = new ModelRenderer(this, 12, 23);
  ear2.addBox(0F, 0F, 0F, 1, 1, 1);
  ear2.setRotationPoint(0.3F, 16.6F, -7F);
  ear2.setTextureSize(64, 32);
  ear2.mirror = true;
  setRotation(ear2, 0F, 0F, 0F);
  var nouse = new ModelRenderer(this, 12, 20);
  nouse.addBox(0F, 0F, 0F, 1, 1, 2);
  nouse.setRotationPoint(-0.5F, 18.5F, -9.5F);
  nouse.setTextureSize(64, 32);
  nouse.mirror = true;
  setRotation(nouse, 0F, 0F, 0F);
  var eye2 = new ModelRenderer(this, 0, 20);
  eye2.addBox(0F, 0F, 0F, 1, 1, 0);
  eye2.setRotationPoint(0.2F, 17.7F, -8.2F);
  eye2.setTextureSize(64, 32);
  eye2.mirror = true;
  setRotation(eye2, 0F, 0F, 0F);

  override def render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
    super.render(entity, f, f1, f2, f3, f4, f5)
    setRotationAngles(entity, f, f1, f2, f3, f4, f5)
    if (isChild) GL11.glScalef(0.5F, 0.5F, 0.5F)
    leg1.render(f5)
    leg3.render(f5)
    leg2.render(f5)
    leg4.render(f5)
    eye.render(f5)
    body.render(f5)
    nouse.render(f5)
    ear2.render(f5)
    ear1.render(f5)
    eye2.render(f5)
    head.render(f5)
    tail.render(f5)
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
