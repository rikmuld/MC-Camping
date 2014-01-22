package rikmuld.camping.entity.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.network.packets.PacketTileData;

public class TileEntityMain extends TileEntity {

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, var1);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
	{
		readFromNBT(packet.data);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
	}

	public void sendTileData(int id, boolean client, int... data)
	{
		if(!client && worldObj.isRemote)
		{
			PacketUtil.sendToSever(new PacketTileData(id, xCoord, yCoord, zCoord, data));
		}
		if(client && !worldObj.isRemote)
		{
			PacketUtil.sendToAllPlayers(new PacketTileData(id, xCoord, yCoord, zCoord, data));
		}
	}

	public void setTileData(int id, int[] data)
	{}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
	}
}
