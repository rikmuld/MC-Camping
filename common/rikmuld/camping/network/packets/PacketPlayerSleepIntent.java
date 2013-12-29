package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.network.PacketTypeHandler;

public class PacketPlayerSleepIntent extends PacketMain {

	int x;
	int y;
	int z;
	
	public PacketPlayerSleepIntent()
	{
		super(PacketTypeHandler.SLEEP, false);
	}

	public PacketPlayerSleepIntent(int x, int y, int z)
	{
		super(PacketTypeHandler.SLEEP, false);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void readData(DataInputStream dataStream) throws IOException
	{
		this.x = dataStream.readInt();
		this.y = dataStream.readInt();
		this.z = dataStream.readInt();
	}

	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(x);
		dataStream.writeInt(y);
		dataStream.writeInt(z);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		((TileEntityTent)player.worldObj.getBlockTileEntity(x, y, z)).sleep(player);
	}
}
