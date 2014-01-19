package rikmuld.camping.client.render.tileentity;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;
import rikmuld.camping.entity.tileentity.TileEntityAntlerThrophy;

public class TileEntityAntlerThrophyRenderer extends TileEntitySpecialRenderer{
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{				 
		TileEntityAntlerThrophy tile = (TileEntityAntlerThrophy) tileentity;
		
		for(int i = 0; i<3; i++)
		{
			GL11.glPushMatrix();
		    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		    RenderHelper.disableStandardItemLighting();			

			GL11.glTranslatef((float) x+0.5F, (float) y+0.03125F, (float) z+0.5F);
			
			GL11.glScalef(1.0F, -1F, -1F);

			if(tile.block[3]<3&&tile.block[3]>0)GL11.glRotatef(tile.block[3]*90, 0, 1, 0);
			else GL11.glRotatef(tile.block[3]!=0? 0:270, 0, 1, 0);
			
			if(i==1)
			{
				GL11.glTranslatef(0.465F, 0.1F, -0.2F);
				GL11.glRotatef(180, -0.1944F, 0.3888F, -0.8888F);
			}
			if(i==2)
			{
				GL11.glTranslatef(1.1F, -0.45F, 0.3F);
				GL11.glRotatef(180, 0.1944F, -0.3888F, -0.8888F);
			}
			
			GL11.glScalef(0.0625F, 0.0625F, 0.0625F);

		    bindTexture(new ResourceLocation(TextureInfo.MODEL_ANTLER_THROPHY));	   
		    
		    if(i==0)ModModels.throphyAntler.renderAllExcept("shape6", "shape7");	  
		    else if(i==1)ModModels.throphyAntler.renderOnly("shape6");
		    else ModModels.throphyAntler.renderOnly("shape7");
		    	
		    GL11.glPopMatrix();
		}

	}
}

/*
 * 6: -35;70;-160
 * 7: 35;-70;-160
 *
 */