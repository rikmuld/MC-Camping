package rikmuld.camping.client.render.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.core.register.ModModels;
import rikmuld.camping.entity.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.entity.tileentity.TileEntityTent;

public class TileEntityTentRenderer extends TileEntitySpecialRenderer{
	
	ItemRenderer renderer = new ItemRenderer(Minecraft.getMinecraft());

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f)
	{
		TileEntityTent tile = (TileEntityTent) tileentity;
		
		GL11.glPushMatrix();
	    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		GL11.glTranslatef((float) x+0.5F, (float) y+0.03125F, (float) z+0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		
	    GL11.glRotatef(tile.rotation+1>3? 90*0:90*(tile.rotation+1), 0, 1, 0);

		bindTexture(new ResourceLocation(TextureInfo.MODEL_TENT));
		
		ModModels.tent.renderAllExcept("bed1", "bed2", "chest1", "chest2", "chest3", "chest4", "chest5", "chest6", "chest7");
		
		if(tile.beds>0)
		{
			if(tile.chests==0)ModModels.tent.renderPart("bed1");
			else ModModels.tent.renderPart("bed2");
		}
		
		if(tile.chests>0)
		{
			if(tile.beds==0)
			{
				if(tile.chests>0)ModModels.tent.renderOnly("chest3");
				if(tile.chests>1)ModModels.tent.renderOnly("chest4");
				if(tile.chests>2)ModModels.tent.renderOnly("chest5");
				if(tile.chests>3)ModModels.tent.renderOnly("chest6");
				if(tile.chests>4)ModModels.tent.renderOnly("chest7");
			}
			else
			{
				if(tile.chests>0)ModModels.tent.renderOnly("chest1");
				if(tile.chests>1)ModModels.tent.renderOnly("chest2");
			}
		}
		
		GL11.glPopMatrix();
		
		if(tile.lanterns>0)
		{			
			GL11.glPushMatrix();
		    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		    
			GL11.glTranslatef((float) x+0.5F, (float) y+0.03125F, (float) z+0.5F);
			
			if(tile.rotation==0||tile.rotation==2)GL11.glRotatef(90, 0F, 1F, 0F);
			GL11.glRotatef(4, 1F, 0F, 0F);

			if(tile.rotation==0||tile.rotation==1)GL11.glTranslatef(-1, 0.9375F, 0.1075F);
			if(tile.rotation==2||tile.rotation==3)GL11.glTranslatef(1, 0.9375F, 0.0925F);

			GL11.glScalef(0.3F, 0.3F, -0.3F);
			GL11.glRotatef(45, 1F, 1F, 0.4F);
			GL11.glRotatef(10, 0.0F, 1F, 0F);
			GL11.glRotatef(5, 0.0F, 0F, -0.2F);

			ItemStack lanternStack = new ItemStack(ModBlocks.lantern.blockID, 1, tile.lanternDamage);
			renderer.renderItem(tileentity.worldObj.getClosestPlayer(x, y, z, 2000), lanternStack, 0);
		
			GL11.glPopMatrix();
		}
		
	}
}
