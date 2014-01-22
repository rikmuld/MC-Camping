package rikmuld.camping.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.network.packets.PacketMain;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	public static ArrayList<Class<? extends PacketMain>> packets = new ArrayList<Class<? extends PacketMain>>();

	public static PacketMain buildPacket(byte[] data)
	{
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		int selector = byteArrayInputStream.read();
		DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
		PacketMain packet = null;

		try
		{
			packet = packets.get(selector).newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}

		packet.readPopulate(dataInputStream);
		return packet;
	}

	public static Packet populatePacket(PacketMain packet)
	{
		byte[] data = packet.populate();
		Packet250CustomPayload packet250 = new Packet250CustomPayload();
		packet250.channel = ModInfo.PACKET_CHANEL;
		packet250.data = data;
		packet250.length = data.length;
		packet250.isChunkDataPacket = packet.isChunkDataPacket;
		return packet250;
	}

	public static void registerNewPacketClass(Class<? extends PacketMain> packet)
	{
		if(!packets.contains(packet))
		{
			packets.add(packet);
		}
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		PacketMain packetMain = buildPacket(packet.data);
		packetMain.execute(manager, (EntityPlayer)player);
	}
}