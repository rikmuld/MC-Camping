package rikmuld.camping.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;
import rikmuld.camping.entity.tileentity.TileEntityBearTrap;

public class TileEntityBearTrapRenderer extends TileEntitySpecialRenderer{
	
	ItemRenderer renderer = new ItemRenderer(Minecraft.getMinecraft());

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntityBearTrap tile = (TileEntityBearTrap) tileentity;
		
		GL11.glPushMatrix();
	    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float) x+0.5F, (float) y+0.03125F, (float) z+0.5F);
		
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		
	    bindTexture(new ResourceLocation(TextureInfo.MODEL_BEARTROP_OPEN));
	  
	    if(tile.open)ModModels.trapOpen.renderAll();
	    else ModModels.trapClosed.renderAll();
		
	    GL11.glPopMatrix();
	  
	    if(tile.getStackInSlot(0)!=null)
	    {
			GL11.glPushMatrix();
	
		    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef( ((float) (x+0.5 + 0.03125F)), (float) y+0.03125F, ((float) (z+0.5 - 0.0625F)));	

			GL11.glRotatef(90, 0, 0, 1);
			
		    GL11.glRotatef(180, 1, 0, 0);
			GL11.glRotatef(-41, 0, 1, 0);
			GL11.glRotatef(-25, -1, 0, 1);
				
			GL11.glScalef(0.1F, -0.1F, -0.1F);

			renderer.renderItem(tileentity.worldObj.getClosestPlayer(x, y, z, -1), tile.getStackInSlot(0), 0);	
			
			GL11.glPopMatrix();
	    }
	}
}
