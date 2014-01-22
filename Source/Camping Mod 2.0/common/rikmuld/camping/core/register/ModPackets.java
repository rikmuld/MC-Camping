package rikmuld.camping.core.register;

import rikmuld.camping.network.PacketHandler;
import rikmuld.camping.network.packets.PacketBounds;
import rikmuld.camping.network.packets.PacketCampInvPos;
import rikmuld.camping.network.packets.PacketItems;
import rikmuld.camping.network.packets.PacketKeyPressed;
import rikmuld.camping.network.packets.PacketMain;
import rikmuld.camping.network.packets.PacketMap;
import rikmuld.camping.network.packets.PacketOpenGui;
import rikmuld.camping.network.packets.PacketPlayerData;
import rikmuld.camping.network.packets.PacketPlayerSleepIntent;
import rikmuld.camping.network.packets.PacketTileData;

public class ModPackets {

	public static void init()
	{
		PacketHandler.registerNewPacketClass(PacketTileData.class);
		PacketHandler.registerNewPacketClass(PacketMain.class);
		PacketHandler.registerNewPacketClass(PacketItems.class);
		PacketHandler.registerNewPacketClass(PacketBounds.class);
		PacketHandler.registerNewPacketClass(PacketMap.class);
		PacketHandler.registerNewPacketClass(PacketOpenGui.class);
		PacketHandler.registerNewPacketClass(PacketPlayerData.class);
		PacketHandler.registerNewPacketClass(PacketCampInvPos.class);
		PacketHandler.registerNewPacketClass(PacketKeyPressed.class);
		PacketHandler.registerNewPacketClass(PacketPlayerSleepIntent.class);
	}
}
