package rikmuld.camping.client.render.tileentity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.client.render.model.AbstractBox;
import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.entity.tileentity.TileEntitySleepingBag;

public class TileEntitySleepingBagRenderer extends TileEntitySpecialRenderer {

	AbstractBox bed = new AbstractBox(64, 32, false, 0, 0, 0, 0, 0, 16, 1, 16, 0.0625F, 0.0F, 0.0F, 0.0F);

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntitySleepingBag tile = (TileEntitySleepingBag)tileentity;

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glTranslatef((float)x, (float)y + 0.0625F, (float)z + 1F);
		GL11.glScalef(1.0F, -1F, -1F);

		GL11.glRotatef(90 * tile.rotation, 0, 1, 0);

		switch(tile.rotation)
		{
			case 1:
			{
				GL11.glTranslatef(-1, 0, 0);
				break;
			}
			case 2:
			{
				GL11.glTranslatef(-1, 0, -1);
				break;
			}
			case 3:
			{
				GL11.glTranslatef(0, 0, -1);
				break;
			}
		}

		if(tile.worldObj.getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord) == 0)
		{
			bindTexture(new ResourceLocation(TextureInfo.MODEL_SLEEPING_TOP));
		}
		else
		{
			bindTexture(new ResourceLocation(TextureInfo.MODEL_SLEEPING_DOWN));
		}

		bed.render(Tessellator.instance);

		GL11.glPopMatrix();
	}
}
