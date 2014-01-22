package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.CampingMod;

public class PacketOpenGui extends PacketMain {

	int id;
	int x = 0;
	int y = 0;
	int z = 0;

	public PacketOpenGui()
	{
		super(false);
	}

	public PacketOpenGui(int id)
	{
		super(false);
		this.id = id;
	}

	public PacketOpenGui(int id, int x, int y, int z)
	{
		super(false);
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		player.openGui(CampingMod.instance, id, player.worldObj, x, y, z);
	}

	@Override
	public void readData(DataInputStream dataStream) throws IOException
	{
		id = dataStream.readInt();
		x = dataStream.readInt();
		y = dataStream.readInt();
		z = dataStream.readInt();
	}

	@Override
	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(id);
		dataStream.writeInt(x);
		dataStream.writeInt(y);
		dataStream.writeInt(z);
	}
}
