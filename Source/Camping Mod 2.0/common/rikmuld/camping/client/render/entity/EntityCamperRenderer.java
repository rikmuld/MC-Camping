package rikmuld.camping.client.render.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.entity.EntityCamper;

public class EntityCamperRenderer extends RenderLiving {

	public EntityCamperRenderer()
	{
		super(new ModelBiped(0.0F), 0.5F);
	}

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		super.doRenderLiving((EntityLiving)entity, d0, d1, d2, f, f1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return ((EntityCamper)entity).getGender() == 0? new ResourceLocation(TextureInfo.MODEL_CAMPER_MALE):new ResourceLocation(TextureInfo.MODEL_CAMPER_FEMALE);
	}
}
