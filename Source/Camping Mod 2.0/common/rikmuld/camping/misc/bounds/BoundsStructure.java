package rikmuld.camping.misc.bounds;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.entity.tileentity.TileEntityBounds;

public class BoundsStructure {
		
	int[][] blocks;
	
	public BoundsStructure(int[][] structure)
	{
		this.blocks = structure;
	}
	
	public void createStructure(World world, BoundsTracker tracker)
	{
		for(int i = 0; i<blocks[0].length; i++)
		{			
			world.setBlock(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i], ModBlocks.bounds.blockID, 0, 2);
			((TileEntityBounds)world.getBlockTileEntity(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i])).setBounds(tracker.getBoundsOnRelativePoistion(blocks[0][i], blocks[1][i], blocks[2][i]));
			((TileEntityBounds)world.getBlockTileEntity(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i])).setBaseCoords(tracker.baseX, tracker.baseY, tracker.baseZ);
		}
	}
	
	public void destroyStructure(World world, BoundsTracker tracker)
	{
		for(int i = 0; i<blocks[0].length; i++)world.setBlock(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i], 0);
	}
	
	public boolean canBePlaced(World world, BoundsTracker tracker)
	{
		for(int i = 0; i<blocks[0].length; i++)
		{
			int blockID = world.getBlockId(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i]);
			if(blockID!=0&&!(Block.blocksList[blockID].isBlockReplaceable(world, tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i])))return false;
		}
		return this.hadSolitUnderGround(world, tracker)? true:false;
	}
	
	public boolean hadSolitUnderGround(World world, BoundsTracker tracker)
	{
		for(int i = 0; i<blocks[0].length; i++)
		{
			if(!world.doesBlockHaveSolidTopSurface(tracker.baseX+blocks[0][i], tracker.baseY-1, tracker.baseZ+blocks[2][i]))return false;
		}
		return true;
	}
}
