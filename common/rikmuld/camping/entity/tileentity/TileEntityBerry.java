package rikmuld.camping.entity.tileentity;

import java.util.Random;

import rikmuld.camping.core.register.ModLogger;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityBerry extends TileEntityMain {

	public boolean berries = false;
	
	int maxTime = 30;
	int time = 0;
	int update = 0;
	
	public Random rand = new Random();
	
	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote&&worldObj.getBlockMetadata(xCoord, yCoord, zCoord)>0)
		{			
			if(time>=maxTime&&!berries)
			{
				berries = true;
				this.sendTileData(0, true, 1);
				worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			
			update++;
			
			if(update>20)
			{
				if(berries==false&&rand.nextInt(10)==0)time++;
				update = 0;
			}
		}
	}
	
	public void getBerries()
	{
		if(!worldObj.isRemote)
		{
			berries = false;
			time = 0;
			this.sendTileData(0, true, 0);
		}
	}
	
	@Override
	public void setTileData(int id, int[] data)
	{
		if(id==0)this.berries = data[0]==1? true:false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		time = tag.getInteger("time");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);	
		tag.setInteger("time", time);
	}
}
