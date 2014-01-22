package rikmuld.camping.core.handler;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.util.KeyUtil;
import rikmuld.camping.core.util.PacketUtil;
import rikmuld.camping.network.packets.PacketKeyPressed;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;

public class KeyHandler extends KeyBindingRegistry.KeyHandler {

	public KeyHandler()
	{
		super(KeyUtil.gatherKeyBindings(), KeyUtil.gatherIsRepeating());
	}

	@Override
	public String getLabel() 
	{
		return ModInfo.MOD_ID + ": " + this.getClass().getSimpleName();
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) 
	{
		if (tickEnd) 
		{
			if (FMLClientHandler.instance().getClient().inGameHasFocus) 
			{
				EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
				if (player != null) 
				{
					KeyUtil.fireKey(true, kb.keyCode, player);
					PacketUtil.sendToSever(new PacketKeyPressed(kb.keyCode, true));
				}
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		if (tickEnd) 
		{
			if (FMLClientHandler.instance().getClient().inGameHasFocus) 
			{
				EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
				if (player != null) 
				{
					KeyUtil.fireKey(false, kb.keyCode, player);
					PacketUtil.sendToSever(new PacketKeyPressed(kb.keyCode, false));
				}
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.CLIENT);
	}
}
