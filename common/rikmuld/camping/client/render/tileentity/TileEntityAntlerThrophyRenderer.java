package rikmuld.camping.client.render.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;

public class TileEntityAntlerThrophyRenderer extends TileEntitySpecialRenderer{
	
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{				 
		GL11.glPushMatrix();
	    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float) x+0.5F, (float) y+0.03125F, (float) z+0.5F);
		
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		
	    bindTexture(new ResourceLocation(TextureInfo.MODEL_ANTLER_THROPHY));
	   
	    ModModels.throphyAntler.renderAll();
	    
	    GL11.glPopMatrix();
	}
}
