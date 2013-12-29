package rikmuld.camping.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityBearRenderer extends RenderLiving
{
    public EntityBearRenderer(ModelBase model)
    {
        super(model, 1);
    }
    
    @Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		GL11.glPushMatrix();
		if(((EntityAgeable)entity).isChild())GL11.glTranslatef(0, -0.75F, 0);
		super.doRenderLiving((EntityLiving) entity, d0, d1, d2, f, f1);
		GL11.glPopMatrix();
	}
    
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return new ResourceLocation(TextureInfo.MODEL_BEAR);
    }
}
