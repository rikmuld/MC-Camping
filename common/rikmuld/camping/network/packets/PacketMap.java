package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.core.handler.EventsHandler;
import rikmuld.camping.network.PacketTypeHandler;

public class PacketMap extends PacketMain {

	public int x;
	public int z;
	public int scale;
	public byte[] colours;

	public PacketMap()
	{
		super(PacketTypeHandler.MAP, false);
	}

	public PacketMap(int scale, int x, int z, byte[] colours)
	{
		super(PacketTypeHandler.MAP, false);
		
		this.x = x;
		this.z = z;
		this.scale = scale;
		this.colours = colours;
	}

	public void readData(DataInputStream dataStream) throws IOException
	{
		this.x = dataStream.readInt();
		this.z = dataStream.readInt();
		this.scale = dataStream.readInt();
		this.colours = new byte[16384];
		dataStream.read(colours);
	}

	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(x);
		dataStream.writeInt(z);
		dataStream.writeInt(scale);
		dataStream.write(colours);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		EventsHandler.map.colorData.put(player, colours);
		EventsHandler.map.posData.put(player, new int[]{scale, x, z});
	}
}
