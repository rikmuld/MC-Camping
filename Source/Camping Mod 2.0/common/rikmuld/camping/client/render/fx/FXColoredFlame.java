package rikmuld.camping.client.render.fx;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FXColoredFlame extends EntityFX {

	int color;
	Random rand = new Random();

	public FXColoredFlame(World world, double x, double y, double z, double mX, double mY, double mZ, int color)
	{
		super(world, x, y, z, mX, mY, mZ);
		motionX = (motionX * 0.009999999776482582D) + mX;
		motionY = (motionY * 0.009999999776482582D) + mY;
		motionZ = (motionZ * 0.009999999776482582D) + mZ;
		particleMaxAge = rand.nextInt(15) + 10;
		particleScale = (rand.nextInt(40) + 60F) / 100F;
		particleTextureIndexX = color % 16;
		particleTextureIndexY = color / 16;
	}

	@Override
	public float getBrightness(float par1)
	{
		float var2 = (particleAge + par1) / particleMaxAge;

		if(var2 < 0.0F)
		{
			var2 = 0.0F;
		}

		if(var2 > 1.0F)
		{
			var2 = 1.0F;
		}

		float var3 = super.getBrightness(par1);
		return (var3 * var2) + (1.0F - var2);
	}

	@Override
	public int getBrightnessForRender(float par1)
	{
		float var2 = (particleAge + par1) / particleMaxAge;

		if(var2 < 0.0F)
		{
			var2 = 0.0F;
		}

		if(var2 > 1.0F)
		{
			var2 = 1.0F;
		}

		int var3 = super.getBrightnessForRender(par1);
		int var4 = var3 & 255;
		int var5 = (var3 >> 16) & 255;
		var4 += (int)(var2 * 15.0F * 16.0F);

		if(var4 > 240)
		{
			var4 = 240;
		}

		return var4 | (var5 << 16);
	}

	@Override
	public int getFXLayer()
	{
		return 0;
	}

	@Override
	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(particleAge++ >= particleMaxAge)
		{
			setDead();
		}

		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9599999785423279D;
		motionY *= 0.9599999785423279D;
		motionZ *= 0.9599999785423279D;

		if(onGround)
		{
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}

	@Override
	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		Tessellator tessellator1 = Tessellator.instance;
		tessellator1.draw();
		tessellator1.startDrawingQuads();
		tessellator1.setBrightness(getBrightnessForRender(par2));
		Minecraft mc = FMLClientHandler.instance().getClient();
		mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.SPRITE_FX));
		float f8 = 1.0F;
		GL11.glPushMatrix();
		tessellator1.setColorOpaque_F(particleRed * f8, particleGreen * f8, particleBlue * f8);
		super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
		tessellator1.draw();
		GL11.glPopMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft:textures/particle/particles.png"));
		tessellator1.startDrawingQuads();
	}
}
