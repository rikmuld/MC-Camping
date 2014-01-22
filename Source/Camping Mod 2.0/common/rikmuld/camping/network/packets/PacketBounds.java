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

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		if(player.worldObj.getBlockTileEntity(x, y, z) != null)
		{
			((TileEntityBounds)player.worldObj.getBlockTileEntity(x, y, z)).bounds = new Bounds(xMin, yMin, zMin, xMax, yMax, zMax);
		}
	}

	@Override
	public void readData(DataInputStream dataStream) throws IOException
	{
		x = dataStream.readInt();
		y = dataStream.readInt();
		z = dataStream.readInt();

		xMin = dataStream.readFloat();
		yMin = dataStream.readFloat();
		zMin = dataStream.readFloat();
		xMax = dataStream.readFloat();
		yMax = dataStream.readFloat();
		zMax = dataStream.readFloat();
	}

	@Override
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
}
