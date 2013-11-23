package rikmuld.camping.misc.bounds;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class Bounds {

	int xMin;
	int yMin;
	int zMin;
	int xMax;
	int yMax;
	int zMax;
	
	public Bounds(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax)
	{
		this.xMin = xMin;
		this.yMin = yMin;
		this.zMin = zMin;
		this.xMax = xMax;
		this.yMax = yMax;
		this.zMax = zMax;
	}
	
	public void setBlockBounds(Block block)
	{
		block.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
	}
	
	public void setBlockCollision(Block block)
	{
		block.setBlockBounds(Math.max(xMin, 0), Math.max(yMin, 0), Math.max(zMin, 0), Math.min(xMax, 1), Math.min(yMax, 1), Math.min(zMax, 1));
	}
}
