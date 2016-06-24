package com.rikmuld.camping.render.fx

import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.FMLClientHandler
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.Lib.TextureInfo
import net.minecraft.entity.Entity
import net.minecraft.client.particle.ParticleFlame
import net.minecraft.client.renderer.VertexBuffer

object ColouredFlame {
  final val TEX = new ResourceLocation(TextureInfo.SPRITE_FX)
  final val TEX_MC = new ResourceLocation("minecraft:textures/particle/particles.png")
}

@SideOnly(Side.CLIENT)
class ColouredFlame(world: World, x: Double, y: Double, z: Double, mX: Double, mY: Double, mZ: Double, color: Int) extends ParticleFlame(world, x, y, z, mX, mY, mZ) {
  setParticleTextureIndex(color)

  override def renderParticle(render: VertexBuffer, entity:Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, par7: Float) {
    val tessellator1 = Tessellator.getInstance
    
    tessellator1.draw()
    GL11.glPushMatrix()
    render.begin(7, render.getVertexFormat());
    
    Minecraft.getMinecraft().renderEngine.bindTexture(ColouredFlame.TEX)
    super.renderParticle(render, entity, par2, par3, par4, par5, par6, par7)
    
    tessellator1.draw()
    GL11.glPopMatrix()
    
    Minecraft.getMinecraft.renderEngine.bindTexture(ColouredFlame.TEX_MC)
    render.begin(7, render.getVertexFormat());
  }
}