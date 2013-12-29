package rikmuld.camping.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.network.packets.PacketBounds;
import rikmuld.camping.network.packets.PacketItems;
import rikmuld.camping.network.packets.PacketMain;
import rikmuld.camping.network.packets.PacketMap;
import rikmuld.camping.network.packets.PacketOpenGui;
import rikmuld.camping.network.packets.PacketPlayerData;
import rikmuld.camping.network.packets.PacketPlayerSleepIntent;
import rikmuld.camping.network.packets.PacketTileData;

public enum PacketTypeHandler
{
	TILEDATA(PacketTileData.class), OPENGUI(PacketOpenGui.class), DATA(PacketPlayerData.class), MAP(PacketMap.class), ITEMS(PacketItems.class), BOUNDS(PacketBounds.class), SLEEP(PacketPlayerSleepIntent.class);

	private Class<? extends PacketMain> clazz;

	PacketTypeHandler(Class<? extends PacketMain> clazz)
	{
		this.clazz = clazz;
	}

	public static PacketMain buildPacket(byte[] data)
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		int selector = bis.read();
		DataInputStream dis = new DataInputStream(bis);
		PacketMain packet = null;
		try
		{
			packet = values()[selector].clazz.newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		packet.readPopulate(dis);
		return packet;
	}

	public static PacketMain buildPacket(PacketTypeHandler type)
	{
		PacketMain packet = null;
		try
		{
			packet = values()[type.ordinal()].clazz.newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		return packet;
	}

	public static Packet populatePacket(PacketMain minetechPacket)
	{
		byte[] data = minetechPacket.populate();
		Packet250CustomPayload packet250 = new Packet250CustomPayload();
		packet250.channel = ModInfo.PACKET_CHANEL;
		packet250.data = data;
		packet250.length = data.length;
		packet250.isChunkDataPacket = minetechPacket.isChunkDataPacket;
		return packet250;
	}
}
