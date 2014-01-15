package rikmuld.camping.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;
import rikmuld.camping.entity.tileentity.TileEntityBarbedWire;

public class TileEntityBarbedWireRenderer extends TileEntitySpecialRenderer{
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{			
		TileEntityBarbedWire tile = (TileEntityBarbedWire) tileentity;
		
		GL11.glPushMatrix();
	    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef((float) x+0.5F, (float) y+0.03125F, (float) z+0.5F);
		
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		
	    bindTexture(new ResourceLocation(TextureInfo.MODEL_BARBED_WIRE));
	   
	    ModModels.wire.renderOnly("middle", "middleVB", "middleVF");

	    if(tile.sides[0])ModModels.wire.renderOnly("backV", "backH");
	    if(tile.sides[1])ModModels.wire.renderOnly("frontV", "frontH");
	    if(tile.sides[2])ModModels.wire.renderOnly("rightV", "rightH");
	    if(tile.sides[3])ModModels.wire.renderOnly("leftV", "leftH");

	    GL11.glPopMatrix();
	}
}
