package rikmuld.camping.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.client.gui.screen.GuiScreenInvExtention;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.core.util.PlayerUtil;
import rikmuld.camping.network.packets.PacketPlayerData;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.Player;

public class PlayerHandler implements IPlayerTracker {

	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{

	}

	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		PacketUtil.sendToPlayer(new PacketPlayerData(player.getEntityData().getCompoundTag("campInv")), (Player)player);
		GuiContendHandler.sendServerContendsToClient(GuiScreenInvExtention.class.getSimpleName(), player, PlayerUtil.getPlayerDataTag(player).getCompoundTag(GuiScreenInvExtention.class.getSimpleName() + ".guiContends"));
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{

	}

	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{

	}
}
