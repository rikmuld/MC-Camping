package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.entity.tileentity.TileEntityBounds;
import rikmuld.camping.misc.bounds.Bounds;

public class PacketBounds extends PacketMain {

	public int x;
	public int y;
	public int z;
	
	public float xMin;
	public float yMin;
	public float zMin;
	public float xMax;
	public float yMax;
	public float zMax;
		
	public PacketBounds()
	{
		super(true);
	}

	public PacketBounds(Bounds bounds, int x, int y, int z)
	{
		super(true);
		this.x = x;
		this.y = y;
		this.z = z;

		xMin = bounds.xMin;
		yMin = bounds.yMin;
		zMin = bounds.zMin;
		xMax = bounds.xMax;
		yMax = bounds.yMax;
		zMax = bounds.zMax;
	}


	public void readData(DataInputStream dataStream) throws IOException
	{
		this.x = dataStream.readInt();
		this.y = dataStream.readInt();
		this.z = dataStream.readInt();

		this.xMin = dataStream.readFloat();
		this.yMin = dataStream.readFloat();
		this.zMin = dataStream.readFloat();
		this.xMax = dataStream.readFloat();
		this.yMax = dataStream.readFloat();
		this.zMax = dataStream.readFloat();
	}

	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(x);
		dataStream.writeInt(y);
		dataStream.writeInt(z);
		
		dataStream.writeFloat(xMin);
		dataStream.writeFloat(yMin);
		dataStream.writeFloat(zMin);
		dataStream.writeFloat(xMax);
		dataStream.writeFloat(yMax);
		dataStream.writeFloat(zMax);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		if(player.worldObj.getBlockTileEntity(x, y, z)!=null)((TileEntityBounds)player.worldObj.getBlockTileEntity(x, y, z)).bounds = new Bounds(xMin, yMin, zMin, xMax, yMax, zMax);
	}
}
