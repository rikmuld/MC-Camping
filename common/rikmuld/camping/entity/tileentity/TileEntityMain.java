package rikmuld.camping.entity.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketTileData;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileEntityMain extends TileEntity {

	public void sendTileData(int id, boolean client, int... data)
	{
		if(!client&&this.worldObj.isRemote)PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketTileData(id, this.xCoord, this.yCoord, this.zCoord, data)));	
		if(client&&!this.worldObj.isRemote)PacketDispatcher.sendPacketToAllPlayers(PacketTypeHandler.populatePacket(new PacketTileData(id, this.xCoord, this.yCoord, this.zCoord, data)));
		else this.setTileData(id, data);
	}

	public void setTileData(int id, int[] data)
	{}

	@Override
	public void readFromNBT(NBTTagCompound NBTTagCompound)
	{
		super.readFromNBT(NBTTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound NBTTagCompound)
	{
		super.writeToNBT(NBTTagCompound);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
	{
		readFromNBT(packet.data);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, var1);
	}
}
