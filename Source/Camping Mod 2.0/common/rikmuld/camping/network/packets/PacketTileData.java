package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.entity.tileentity.TileEntityMain;

public class PacketTileData extends PacketMain {

	public int x;
	public int y;
	public int z;
	public int id;
	public int length;
	public byte[] data;

	public PacketTileData()
	{
		super(true);
	}

	public PacketTileData(int id, int x, int y, int z, int... data)
	{
		super(true);
		
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.length = data.length*4;
		this.data = new byte[data.length*4];

		for(int i = 0; i<data.length; i++)
		{
			byte[] intData = ByteBuffer.allocate(4).putInt(data[i]).array();
			
			for(int o = 0; o<4; o++)
			{
				this.data[i*4+o] = intData[o];
			}
		}
	}

	public void readData(DataInputStream dataStream) throws IOException
	{
		this.id = dataStream.readInt();
		this.x = dataStream.readInt();
		this.y = dataStream.readInt();
		this.z = dataStream.readInt();
		this.length = dataStream.readInt();
		this.data = new byte[length];
		dataStream.read(data);
	}

	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(id);
		dataStream.writeInt(x);
		dataStream.writeInt(y);
		dataStream.writeInt(z);
		dataStream.writeInt(length);
		dataStream.write(data);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		int[] intData = new int[length/4];
		
		for(int i = 0; i<length/4; i++)
		{
			byte[] wrap = new byte[4];
			for(int o = i*4; o<i*4+4; o++)
			{
				wrap[o-i*4] = data[o];
			}
			intData[i] = ByteBuffer.wrap(wrap).getInt();
		}
		
		if(player.worldObj.getBlockTileEntity(x, y, z)!=null)((TileEntityMain) player.worldObj.getBlockTileEntity(x, y, z)).setTileData(id, intData);
	}
}
