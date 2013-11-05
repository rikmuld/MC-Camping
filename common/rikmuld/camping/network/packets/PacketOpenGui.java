package rikmuld.camping.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.tileentity.TileEntityMain;
import cpw.mods.fml.common.network.Player;

public class PacketOpenGui extends PacketMain {

	int id;
	
	public PacketOpenGui()
	{
		super(PacketTypeHandler.OPENGUI, false);
	}

	public PacketOpenGui(int id)
	{
		super(PacketTypeHandler.OPENGUI, false);
		this.id = id;
	}
	
	public void readData(DataInputStream dataStream) throws IOException
	{
		this.id = dataStream.readInt();
	}

	public void writeData(DataOutputStream dataStream) throws IOException
	{
		dataStream.writeInt(id);
	}

	@Override
	public void execute(INetworkManager network, EntityPlayer player)
	{
		player.openGui(CampingMod.instance, id, player.worldObj, 0, 0, 0);
	}
}
