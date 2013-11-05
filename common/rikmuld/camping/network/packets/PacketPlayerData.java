package rikmuld.camping.network.packets;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.CampingMod;
import rikmuld.camping.network.PacketTypeHandler;

public class PacketPlayerData extends PacketMain {

	NBTTagCompound data;

	public PacketPlayerData()
	{
		super(PacketTypeHandler.DATA, false);
	}

	public PacketPlayerData(NBTTagCompound tag)
	{
		super(PacketTypeHandler.DATA, false);
		this.data = tag;
	}

	public void readData(DataInputStream dataStream) throws IOException
	{
		this.data = this.readNBTTagCompound(dataStream);
	}

	public void writeData(DataOutputStream dataStream) throws IOException
	{
		this.writeNBTTagCompound(data, dataStream);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		player.getEntityData().setCompoundTag("campInv", data);
	}
}
