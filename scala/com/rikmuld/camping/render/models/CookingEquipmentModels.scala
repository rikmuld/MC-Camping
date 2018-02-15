package com.rikmuld.camping.render.models

import com.rikmuld.corerm.client.ModularModal

//TODO finish
object CookingEquipmentModels {
  final val SPIT =
    new ModularModal()

  final val GRILL =
    new ModularModal()

  final val PAN =
    new ModularModal()

//  GRILL.setTextureSize(128, 32)
//  GRILL.addBox("pillar", 0, 2, 0, 0, 0, 1, 16, 1, 0.03125F)
//  GRILL.addBox("line", 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F)
//  GRILL.addBox("line2", 0, 0, 0, 0, 0, 1, 1, 60, 0.015625F)
//
//  GRILL.setTextureSize(64, 64) //HOW, merge textures, or just render first three and these two serperate switdch texutres easy
//  GRILL.addBox("sLine", 0, 0, 0, 0, 0, 29, 1, 1, 0.015625F)
//  GRILL.addBox("sLine2", 0, 0, 0, 0, 0, 1, 1, 29, 0.015625F)
//
//  //grill, get x y z from this
//  override def renderModel() {
//    GL11.glPushMatrix()
//    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_SPIT))
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.4375F, -0.5F, -0.015625F)
//    pilar.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(0.40625F, -0.5F, -0.015625F)
//    pilar.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.0234375F, -0.5F, -0.4375F)
//    pilar.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.0234375F, -0.5F, 0.40625F)
//    pilar.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.46875F, -0.4375F, -0.0078125F)
//    line.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.015625F, -0.4375F, -0.46875F)
//    line2.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_GRILL))
//    for (i <- 0 until 15) {
//      GL11.glPushMatrix()
//      GL11.glTranslatef(-0.2343675F, -0.4453125F, -0.2265625F + (i * 0.03125F))
//      sLine.render(Tessellator.getInstance.getBuffer)
//      GL11.glPopMatrix()
//    }
//    for (i <- 0 until 15) {
//      GL11.glPushMatrix()
//      GL11.glTranslatef(-0.2343675F + (i * 0.03125F), -0.4453125F, -0.2265625F)
//      sLine2.render(Tessellator.getInstance.getBuffer)
//      GL11.glPopMatrix()
//    }
//    GL11.glPopMatrix()
//  }
//
//  //PAN stuff
//  var pilar: AbstractBox = new AbstractBox(64, 32, false, 0, 2, 0, 0, 0, 1, 28, 1, 0.03125F, 0.0F, 0.0F, 0.0F)
//  var line: AbstractBox = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)
//  var cable: AbstractBox = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 1, 45, 1, 0.0078625F, 0.0F, 0.0F, 0.0F)
//  var pan: AbstractBox = new AbstractBox(32, 16, false, 2, 1, 0, 0, 0, 5, 3, 5, 0.0625F, 0.0F, 0.0F, 0.0F)
//  var panCover: AbstractBox = new AbstractBox(64, 32, false, 4, 2, 0, 0, 0, 3, 1, 3, 0.0625F, 0.0F, 0.0F, 0.0F)
//  var panHandle: AbstractBox = new AbstractBox(64, 32, false, 4, 13, 0, 0, 0, 1, 1, 1, 0.03125F, 0.0F, 0.0F, 0.0F)
//
//
//  override def renderModel() {
//    GL11.glPushMatrix()
//    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_PAN))
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.4375F, -0.875F, -0.015625F)
//    pilar.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(0.40625F, -0.875F, -0.015625F)
//    pilar.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.46875F, -0.859375F, -0.0078125F)
//    line.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.15625F, -0.53125F, -0.15625F)
//    pan.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.09375F, -0.5625F, -0.09375F)
//    panCover.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.015625F, -0.578125F, -0.015625F)
//    panHandle.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_GRILL))
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.00393125F, -0.8515125F, -0.00393125F)
//    cable.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPopMatrix()
//  }
//
//  //Spit
//  var pilar: AbstractBox = new AbstractBox(128, 32, false, 0, 2, 0, 0, 0, 1, 16, 1, 0.03125F, 0.0F, 0.0F, 0.0F)
//  var line: AbstractBox = new AbstractBox(128, 32, false, 0, 0, 0, 0, 0, 60, 1, 1, 0.015625F, 0.0F, 0.0F, 0.0F)
//
//  override def renderModel() {
//    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation(TextureInfo.MODEL_SPIT))
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.4375F, -0.5F, -0.015625F)
//    pilar.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(0.40625F, -0.5F, -0.015625F)
//    pilar.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//    GL11.glPushMatrix()
//    GL11.glTranslatef(-0.46875F, -0.484375F, -0.0078125F)
//    line.render(Tessellator.getInstance.getBuffer)
//    GL11.glPopMatrix()
//  }
}
