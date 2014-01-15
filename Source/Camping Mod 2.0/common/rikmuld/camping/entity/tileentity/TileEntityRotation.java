package rikmuld.camping.entity.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityRotation extends TileEntityInventory{

	public int rotation;
	
	public void setRotation(int rotation)
	{		
		if(!worldObj.isRemote)
		{
			this.rotation = rotation; 		
			this.sendTileData(0, true, this.rotation);		
		}
	}
	
	public void cycleRotation()
	{
		if(!worldObj.isRemote)this.setRotation(this.rotation<3? this.rotation+1:0);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		this.rotation = tag.getInteger("rotation");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("rotation", rotation);
	}
	
	@Override
	public void setTileData(int id, int[] data)
	{
		if(id==0)
		{
			this.rotation = data[0];
			
			worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, 0);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, 0);
			worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return 0;
	}
}
