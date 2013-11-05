package rikmuld.camping.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import rikmuld.camping.network.packets.PacketMain;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		PacketMain packetMain = PacketTypeHandler.buildPacket(packet.data);
		packetMain.execute(manager, (EntityPlayer)player);
	}
}