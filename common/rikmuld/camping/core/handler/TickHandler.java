package rikmuld.camping.core.handler;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import rikmuld.camping.CampingMod;
import rikmuld.camping.client.gui.container.GuiContainerPlayerInv;
import rikmuld.camping.client.gui.screen.GuiScreenMapHUD;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModLogger;
import rikmuld.camping.core.util.CampingInvUtil;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketMap;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TickHandler implements ITickHandler{

	private int tickLight;
	private boolean sync = false;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if(type.equals(EnumSet.of(TickType.PLAYER)))
		{			
			EntityPlayer player = (EntityPlayer) tickData[0];
			World world = player.worldObj;
		
			if(world.isRemote&&Minecraft.getMinecraft().currentScreen!=null&&Minecraft.getMinecraft().currentScreen instanceof GuiInventory&&!(Minecraft.getMinecraft().currentScreen instanceof GuiContainerPlayerInv))
			{
				player.openGui(CampingMod.instance, GuiInfo.GUI_INV_PLAYER, world, 0, 0, 0);
			}
				
			if(!world.isRemote&&CampingInvUtil.hasLantarn(player))
			{
				tickLight++;
				if(tickLight>=10)
				{
					tickLight = 0;
					CampingInvUtil.lanternTick(player);
					if(player.worldObj.getBlockId((int)player.posX, (int)player.posY-1, (int)player.posZ)==0)player.worldObj.setBlock((int)player.posX, (int)player.posY-1, (int)player.posZ, ModBlocks.light.blockID);
					else if(player.worldObj.getBlockId((int)player.posX, (int)player.posY, (int)player.posZ)==0)player.worldObj.setBlock((int)player.posX, (int)player.posY, (int)player.posZ, ModBlocks.light.blockID);
					else if(player.worldObj.getBlockId((int)player.posX, (int)player.posY+1, (int)player.posZ)==0)player.worldObj.setBlock((int)player.posX, (int)player.posY+1, (int)player.posZ, ModBlocks.light.blockID);
				}
			}
			
			if(!world.isRemote&&CampingInvUtil.hasMap(player))
			{				
				MapData data = CampingInvUtil.getMapData(player);
				PacketDispatcher.sendPacketToPlayer(PacketTypeHandler.populatePacket(new PacketMap(data.scale, data.xCenter, data.zCenter, data.colors)), (Player) player);
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel()
	{
		return ModInfo.MOD_ID+": "+this.getClass().getSimpleName();
	}
}
