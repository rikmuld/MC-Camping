package rikmuld.camping.misc.bounds;

import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.entity.tileentity.TileEntityBounds;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BoundsStructure {
	
	BoundsTracker tracker;
	World world;
	
	int[][] blocks;
	
	public BoundsStructure(BoundsTracker tracker, World world, int[][] structure)
	{
		this.tracker = tracker;
		this.world = world;
		this.blocks = structure;
	}
	
	public void createStructure()
	{
		for(int i = 0; i<blocks[0].length; i++)
		{
			world.setBlock(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i], ModBlocks.bounds.blockID, 0, 2);
			((TileEntityBounds)world.getBlockTileEntity(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i])).setBounds(tracker.getBoundsOnRelativePoistion(blocks[0][i], blocks[1][i], blocks[2][i]));
			((TileEntityBounds)world.getBlockTileEntity(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i])).setBaseCoords(tracker.baseX, tracker.baseY, tracker.baseZ);
		}
	}
	
	public void destroyStructure()
	{
		for(int i = 0; i<blocks[0].length; i++)world.setBlock(tracker.baseX+blocks[0][i], tracker.baseY+blocks[1][i], tracker.baseZ+blocks[2][i], 0);
	}
}
