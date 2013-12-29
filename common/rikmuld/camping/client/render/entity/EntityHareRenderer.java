package rikmuld.camping.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.entity.EntityHare;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityHareRenderer extends RenderLiving
{
    public EntityHareRenderer(ModelBase model)
    {
        super(model, 0.2F);
    }
    
    @Override
	public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
	{
		GL11.glPushMatrix();
		if(((EntityAgeable)entity).isChild())GL11.glTranslatef(0, -0.75F, 0);
		super.doRenderLiving((EntityLiving) entity, d0, d1, d2, f, f1);
		GL11.glPopMatrix();
	}
    
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return  ((EntityHare)entity).getType()==0? new ResourceLocation(TextureInfo.MODEL_HARE):new ResourceLocation(TextureInfo.MODEL_HARE2);
    }
}
