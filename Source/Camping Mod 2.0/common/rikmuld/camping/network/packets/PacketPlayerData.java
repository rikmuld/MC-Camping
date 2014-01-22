package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;

public class PacketPlayerData extends PacketMain {

	NBTTagCompound data;

	public PacketPlayerData()
	{
		super(false);
	}

	public PacketPlayerData(NBTTagCompound tag)
	{
		super(false);
		data = tag;
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		player.getEntityData().setCompoundTag("campInv", data);
	}

	@Override
	public void readData(DataInputStream dataStream) throws IOException
	{
		data = readNBTTagCompound(dataStream);
	}

	@Override
	public void writeData(DataOutputStream dataStream) throws IOException
	{
		writeNBTTagCompound(data, dataStream);
	}
}
