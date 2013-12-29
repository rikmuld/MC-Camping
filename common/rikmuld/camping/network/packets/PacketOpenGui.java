package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.CampingMod;
import rikmuld.camping.network.PacketTypeHandler;

public class PacketOpenGui extends PacketMain {

	int id;
	int x = 0;
	int y = 0;
	int z = 0;
	
	public PacketOpenGui()
	{
		super(PacketTypeHandler.OPENGUI, false);
	}

	public PacketOpenGui(int id)
	{
		super(PacketTypeHandler.OPENGUI, false);
		this.id = id;
	}
	
	public PacketOpenGui(int id, int x, int y, int z)
	{
		super(PacketTypeHandler.OPENGUI, false);
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void readData(DataInputStream dataStream) throws IOException
	{
		this.id = dataStream.readInt();
		this.x = dataStream.readInt();
		this.y = dataStream.readInt();
		this.z = dataStream.readInt();
	}

	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(id);
		dataStream.writeInt(x);
		dataStream.writeInt(y);
		dataStream.writeInt(z);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		player.openGui(CampingMod.instance, id, player.worldObj, x, y, z);
	}
}
