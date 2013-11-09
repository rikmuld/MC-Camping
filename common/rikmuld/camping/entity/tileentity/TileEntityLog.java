package rikmuld.camping.entity.tileentity;

import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.entity.EntityMountableBlock;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityLog extends TileEntityRotation {
	
	public boolean update = false;
	public EntityMountableBlock mountable;
	
	public TileEntityLog()
	{
		super();
		update = true;
	}
	
	public void updateEntity()
	{
		if(update)
		{
			mountable = new EntityMountableBlock(worldObj, xCoord, yCoord, zCoord);
			worldObj.spawnEntityInWorld(mountable);
			
			this.update = false;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1);
	}
}
