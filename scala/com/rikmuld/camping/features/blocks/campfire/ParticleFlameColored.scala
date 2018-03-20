package com.rikmuld.camping.features.blocks.campfire

import com.rikmuld.camping.Library.TextureInfo
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleFlame
import net.minecraft.client.renderer.{BufferBuilder, Tessellator}
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import org.lwjgl.opengl.GL11

object ParticleFlameColored {
  final val TEX = new ResourceLocation(TextureInfo.SPRITE_FX)
  final val TEX_MC = new ResourceLocation("minecraft:textures/particle/particles.png")

  @SideOnly(Side.CLIENT)
  def renderAt(world: World, x: Double, y: Double, z: Double,
               motionX: Double, motionY: Double, motionZ: Double,
               color: Int): Unit =

    FMLClientHandler.instance().getClient.effectRenderer.addEffect(
      new ParticleFlameColored(world, x, y, z, motionX, motionY, motionZ, color)
    )
}

@SideOnly(Side.CLIENT)
class ParticleFlameColored(world: World, x: Double, y: Double, z: Double, mX: Double, mY: Double, mZ: Double, color: Int) extends ParticleFlame(world, x, y, z, mX, mY, mZ) {
  setParticleTextureIndex(color)

  override def renderParticle(render: BufferBuilder, entity:Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, par7: Float) {
    val tessellator1 = Tessellator.getInstance
    
    tessellator1.draw()
    GL11.glPushMatrix()
    render.begin(7, render.getVertexFormat)
    
    Minecraft.getMinecraft.renderEngine.bindTexture(ParticleFlameColored.TEX)
    super.renderParticle(render, entity, par2, par3, par4, par5, par6, par7)
    
    tessellator1.draw()
    GL11.glPopMatrix()
    
    Minecraft.getMinecraft.renderEngine.bindTexture(ParticleFlameColored.TEX_MC)
    render.begin(7, render.getVertexFormat)
  }
}