package rikmuld.camping.entity.tileentity;

import cpw.mods.fml.common.network.PacketDispatcher;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.misc.bounds.Bounds;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketBounds;

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
}
