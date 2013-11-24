package rikmuld.camping.misc.bounds;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class Bounds {

	public float xMin;
	public float yMin;
	public float zMin;
	public float xMax;
	public float yMax;
	public float zMax;
	
	public Bounds(float xMin, float yMin, float zMin, float xMax, float yMax, float zMax)
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

	public String getLog()
	{
		return "Bounds: "+xMin+" "+yMin+" "+zMin+" "+xMax+" "+yMax+" "+zMax+" ";
	}
}
