package rikmuld.camping.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketPlayerData;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PlayerHandler implements IPlayerTracker {

	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		 PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketPlayerData(player.getEntityData().getCompoundTag("campInv"))), (Player) player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{
		
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{
		
	}
}
