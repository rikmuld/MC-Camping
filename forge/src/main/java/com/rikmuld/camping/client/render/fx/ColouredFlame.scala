package com.rikmuld.camping.client.render.fx

import java.util.Random
import org.lwjgl.opengl.GL11
import com.rikmuld.camping.core.TextureInfo
import cpw.mods.fml.client.FMLClientHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.EntityFX
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side

@SideOnly(Side.CLIENT)
class ColoredFlame(world: World, x: Double, y: Double, z: Double, mX: Double, mY: Double, mZ: Double, color: Int) extends EntityFX(world, x, y, z, mX, mY, mZ) {  
  motionX = (motionX * 0.009999999776482582D) + mX
  motionY = (motionY * 0.009999999776482582D) + mY
  motionZ = (motionZ * 0.009999999776482582D) + mZ
  particleMaxAge = rand.nextInt(15) + 10
  particleScale = (rand.nextInt(40) + 60F) / 100F
  particleTextureIndexX = color % 16
  particleTextureIndexY = color / 16

  override def getBrightness(par1: Float): Float = {
    var var2 = (particleAge + par1) / particleMaxAge
    if (var2 < 0.0F) var2 = 0.0F
    if (var2 > 1.0F) var2 = 1.0F
    val var3 = super.getBrightness(par1)
    (var3 * var2) + (1.0F - var2)
  }
  override def getBrightnessForRender(par1: Float): Int = {
    var var2 = (particleAge + par1) / particleMaxAge
    if (var2 < 0.0F) var2 = 0.0F
    if (var2 > 1.0F) var2 = 1.0F
    val var3 = super.getBrightnessForRender(par1)
    var var4 = var3 & 255
    val var5 = (var3 >> 16) & 255
    var4 += (var2 * 15.0F * 16.0F).toInt
    if (var4 > 240) var4 = 240
    var4 | (var5 << 16)
  }
  override def getFXLayer(): Int = 0
  override def onUpdate() {
    prevPosX = posX
    prevPosY = posY
    prevPosZ = posZ
    particleAge+=1;
  
    if (particleAge >= particleMaxAge) setDead()
    moveEntity(motionX, motionY, motionZ)
    
    motionX *= 0.9599999785423279D
    motionY *= 0.9599999785423279D
    motionZ *= 0.9599999785423279D
    if (onGround) {
      motionX *= 0.699999988079071D
      motionZ *= 0.699999988079071D
    }
  }
  override def renderParticle(par1Tessellator: Tessellator, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, par7: Float) {
    val tessellator1 = Tessellator.instance
    tessellator1.draw()
    tessellator1.startDrawingQuads()
    tessellator1.setBrightness(getBrightnessForRender(par2))
    val mc = FMLClientHandler.instance().getClient
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.SPRITE_FX))
    val f8 = 1.0F
    GL11.glPushMatrix()
    tessellator1.setColorOpaque_F(particleRed * f8, particleGreen * f8, particleBlue * f8)
    super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7)
    tessellator1.draw()
    GL11.glPopMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(new ResourceLocation("minecraft:textures/particle/particles.png"))
    tessellator1.startDrawingQuads()
  }
}