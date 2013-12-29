package rikmuld.camping.entity.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import rikmuld.camping.misc.bounds.Bounds;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketBounds;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileEntityBounds extends TileEntityMain {

	public Bounds bounds;
	
	public int baseX;
	public int baseY;
	public int baseZ;

	private boolean update;
	
	public void updateEntity()
	{
		if(!worldObj.isRemote&&update)
		{
			PacketDispatcher.sendPacketToAllPlayers(PacketTypeHandler.populatePacket(new PacketBounds(bounds, xCoord, yCoord, zCoord)));
			update=false;
		}
	}
	
	public void setBounds(Bounds bounds)
	{
		this.bounds = bounds;
		this.update = true;
	}
	
	public void setBaseCoords(int x, int y, int z)
	{
		this.baseX = x;
		this.baseY = y;
		this.baseZ = z;
		
		this.sendTileData(0, true, x, y, z);
	}
	
	@Override
	public void setTileData(int id, int[] data)
	{
		if(id==0)
		{
			this.baseX = data[0];
			this.baseY = data[1];
			this.baseZ = data[2];
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		
		this.baseX = tag.getInteger("baseX");
		this.baseY = tag.getInteger("baseY");
		this.baseZ = tag.getInteger("baseZ");
		if(tag.hasKey("xMin"))this.setBounds(Bounds.readBoundsToNBT(tag));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{	
		super.writeToNBT(tag);
		
		tag.setInteger("baseX", baseX);
		tag.setInteger("baseY", baseY);
		tag.setInteger("baseZ", baseZ);
		if(this.bounds!=null)this.bounds.writeBoundsToNBT(tag);
	}
}
