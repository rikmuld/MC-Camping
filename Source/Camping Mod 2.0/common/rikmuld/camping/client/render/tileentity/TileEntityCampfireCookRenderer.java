package rikmuld.camping.client.render.tileentity;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModModels;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;

public class TileEntityCampfireCookRenderer extends TileEntitySpecialRenderer{

	ItemRenderer renderer = new ItemRenderer(Minecraft.getMinecraft());
	Random rand = new Random();
	String[] strings = new String[]{"side1", "side2", "side3", "side4", "corner1", "corner2", "corner3", "corner4"};
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntityCampfireCook tile = (TileEntityCampfireCook) tileentity;
		
		GL11.glPushMatrix();
	    GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glTranslatef((float) x+0.5F, (float) y+0.03125F, (float) z+0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		
		if(tile.equipment!=null)
		{
			tile.equipment.renderModel();
			EntityLivingBase entity = tileentity.worldObj.getClosestPlayer(x, y, z, 2000);
			for(int i = 0; i<tile.equipment.maxFood; i++)if(tile.getStackInSlot(i+2)!=null)tile.equipment.renderFood(i, tile.getStackInSlot(i+2), entity);
		}
		
		bindTexture(new ResourceLocation(TextureInfo.MODEL_CAMPFIRE_DECO));
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		
		for(String string:strings)ModModels.campfire.renderPart(string);
		
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x+0.4F, (float) y+0.0625f, (float) z+0.4F);
		
		ItemStack coalItem = new ItemStack(Item.coal.itemID, 1, 0);
		
		for(int i = 0; i<tile.getCoalPieces(); i++)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(tile.coals[0][i], 0, tile.coals[1][i]);
			GL11.glScalef(0.2F, 0.15F, 0.15F);
			GL11.glRotatef(tile.coals[2][i], 0, 1, 0);
			GL11.glRotatef(45, 1F, 1F, 0.4F);
			GL11.glRotatef(10, 0.0F, 1F, 0F);
			GL11.glRotatef(5, 0.0F, 0F, -0.2F);
			renderer.renderItem(tileentity.worldObj.getClosestPlayer(x, y, z, 2000), coalItem, 0);
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
	}
}
