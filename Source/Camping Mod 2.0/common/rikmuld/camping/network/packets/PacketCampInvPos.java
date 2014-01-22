package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.core.util.PlayerUtil;

public class PacketCampInvPos extends PacketMain {

	String name;
	NBTTagCompound tag;

	public PacketCampInvPos()
	{
		super(true);
	}

	public PacketCampInvPos(String name, NBTTagCompound tag)
	{
		super(true);
		this.name = name;
		this.tag = tag;
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		PlayerUtil.getPlayerDataTag(player).setCompoundTag(name, tag);
	}

	@Override
	public void readData(DataInputStream dataStream) throws IOException
	{
		name = dataStream.readUTF();
		tag = readNBTTagCompound(dataStream);
	}

	@Override
	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeUTF(name);
		writeNBTTagCompound(tag, dataStream);
	}
}
