package rikmuld.camping.entity.tileentity;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import rikmuld.camping.block.BlockTent;
import rikmuld.camping.core.util.MathUtil;
import rikmuld.camping.misc.bounds.Bounds;
import rikmuld.camping.misc.bounds.BoundsStructure;
import rikmuld.camping.misc.bounds.BoundsTracker;

public class TileEntityTent extends TileEntityRotation{

	public BoundsStructure[] structures = new BoundsStructure[4];
	int[][][] structureBlocks = new int[4][3][1];
	public Bounds[] bounds = new Bounds[]{new Bounds(-0.5F, 0, 0, 1.5F, 1.5F, 3), new Bounds(-2, 0, -0.5F, 1, 1.5F, 1.5F), new Bounds(-0.5F, 0, -2, 1.5F, 1.5F, 1), new Bounds(0, 0, -0.5F, 3, 1.5F, 1.5F)};
	
	boolean isNew = true;
	
	public void updateEntity()
	{
		if(isNew)
		{
			this.initalize();
			this.isNew = false;
		}
	}
	
	public void initalize()
	{
		if(!worldObj.isRemote)
		{
			int[] xLine = new int[]{1, -1, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0};
			int[] yLine = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1};
			int[] zLine = new int[]{0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2};

			structureBlocks[0] = new int[][]{xLine, yLine, zLine};
			structureBlocks[1] = new int[][]{MathUtil.inverse(zLine) ,yLine, MathUtil.inverse(xLine)};
			structureBlocks[2] = new int[][]{MathUtil.inverse(xLine) ,yLine, MathUtil.inverse(zLine)};
			structureBlocks[3] = new int[][]{zLine ,yLine, xLine};
					
			for(int i = 0; i<4; i++)structures[i] = new BoundsStructure(new BoundsTracker(xCoord, yCoord, zCoord, bounds[i]), worldObj, structureBlocks[i]);
		}
	}
	
	public void setRotation(int rotation)
	{				
		if(!worldObj.isRemote)
		{
			if(isNew)
			{
				this.initalize();
				this.isNew = false;
			}
			else structures[this.rotation].destroyStructure();
			
			this.rotation = rotation; 		
			this.sendTileData(0, true, this.rotation);		
			
			structures[this.rotation].createStructure();
		}
	}
	
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        AxisAlignedBB bb = INFINITE_EXTENT_AABB;
        Bounds bound = this.bounds[this.rotation];
        bb.getBoundingBox(bound.xMin, bound.yMin, bound.zMin, bound.xMax, bound.yMax, bound.zMax);
        return bb;
    }
}
