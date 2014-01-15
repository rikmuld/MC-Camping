package rikmuld.camping.core.util;

import rikmuld.camping.network.PacketHandler;
import rikmuld.camping.network.packets.PacketMain;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketUtil {

	public static void sendToSever(PacketMain packet)
	{
		PacketDispatcher.sendPacketToServer(PacketHandler.populatePacket(packet));
	}
	
	public static void sendToAllPlayers(PacketMain packet)
	{
		PacketDispatcher.sendPacketToAllPlayers(PacketHandler.populatePacket(packet));
	}

	public static void sendToPlayer(PacketMain packet, Player player)
	{
		PacketDispatcher.sendPacketToPlayer(PacketHandler.populatePacket(packet), player);
	}

	public static void sendToAllAround(PacketMain packet, double x, double y, double z, int range, int dimensionId)
	{
		PacketDispatcher.sendPacketToAllAround(x, y, z, range, dimensionId, PacketHandler.populatePacket(packet));
	}

	public static void sendToAllInDimension(PacketMain packet, int dimensionId)
	{
		PacketDispatcher.sendPacketToAllInDimension(PacketHandler.populatePacket(packet), dimensionId);
	}
}