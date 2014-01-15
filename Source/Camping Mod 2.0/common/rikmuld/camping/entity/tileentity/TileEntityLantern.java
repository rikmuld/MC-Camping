package rikmuld.camping.entity.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityLantern extends TileEntityMain{

	public int burnTime;
	int update;
	
	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{
			update++;
			if(update>=10)
			{
				update = 0;
				this.burnTime--;
			}
			
			if(burnTime<=0)
			{
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		burnTime = tag.getInteger("burnTime");
		super.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger("burnTime", burnTime);
		super.writeToNBT(tag);
	}
}
