package rikmuld.camping.entity.tileentity;

import net.minecraft.util.AxisAlignedBB;
import rikmuld.camping.entity.EntityMountableBlock;
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

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
	}

	@Override
	public void updateEntity()
	{
		if(update)
		{
			mountable = new EntityMountableBlock(worldObj, xCoord, yCoord, zCoord);
			worldObj.spawnEntityInWorld(mountable);

			update = false;
		}
	}
}
