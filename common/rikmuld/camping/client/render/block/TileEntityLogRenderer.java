package rikmuld.camping.client.render.block;

import java.nio.FloatBuffer;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.model.techne.TechneModel;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import rikmuld.camping.core.lib.TextureInfo;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModModels;
import rikmuld.camping.core.util.BlockUtil;
import rikmuld.camping.entity.tileentity.TileEntityLog;

public class TileEntityLogRenderer extends TileEntitySpecialRenderer{
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float fl)
	{
		GL11.glPushMatrix();

		TechneModel model = ModModels.log;
		
		GL11.glTranslatef((float) x+0.5F, (float) y+0.03125F, (float) z+0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		
		TileEntityLog log = (TileEntityLog) tileentity;
		World world = log.worldObj;
		int xPos = log.xCoord;
		int yPos = log.yCoord;
		int zPos = log.zCoord;
		
		int[][] blockInfo = BlockUtil.getBlocks(world, xPos, yPos, zPos);
		
		int meta = (log.rotation+1)%2;
						
	    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		if(meta==0)
		{
			this.bindTexture(new ResourceLocation(TextureInfo.MODEL_LOG));

			model.renderPart("Base");
			model.renderPart("BaseBack");
			model.renderPart("BaseFront");
			
			if(blockInfo[ForgeDirection.NORTH.ordinal()][0]==ModBlocks.log.blockID&&(((TileEntityLog)world.getBlockTileEntity(xPos, yPos, zPos-1)).rotation+1)%2==0) model.renderPart("ExtentionBack");
			if(blockInfo[ForgeDirection.SOUTH.ordinal()][0]==ModBlocks.log.blockID&&(((TileEntityLog)world.getBlockTileEntity(xPos, yPos, zPos+1)).rotation+1)%2==0) model.renderPart("ExtentionFront");
		}
		
		if(meta==1)
		{
			this.bindTexture(new ResourceLocation(TextureInfo.MODEL_LOG2));

			model.renderPart("Base");
			model.renderPart("BaseLeft");
			model.renderPart("BaseRight");

			if(blockInfo[ForgeDirection.WEST.ordinal()][0]==ModBlocks.log.blockID&&(((TileEntityLog)world.getBlockTileEntity(xPos-1, yPos, zPos)).rotation+1)%2==1) model.renderPart("ExtentionRight");
			if(blockInfo[ForgeDirection.EAST.ordinal()][0]==ModBlocks.log.blockID&&(((TileEntityLog)world.getBlockTileEntity(xPos+1, yPos, zPos)).rotation+1)%2==1) model.renderPart("ExtentionLeft");
		}
		
		GL11.glPopMatrix();
	}
}
