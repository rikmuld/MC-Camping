package rikmuld.camping.entity.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.network.packets.PacketTileData;

public class TileEntityMain extends TileEntity {

	public void sendTileData(int id, boolean client, int... data)
	{
		if(!client&&this.worldObj.isRemote)PacketUtil.sendToSever(new PacketTileData(id, this.xCoord, this.yCoord, this.zCoord, data));	
		if(client&&!this.worldObj.isRemote)PacketUtil.sendToAllPlayers(new PacketTileData(id, this.xCoord, this.yCoord, this.zCoord, data));
	}

	public void setTileData(int id, int[] data)
	{}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
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
