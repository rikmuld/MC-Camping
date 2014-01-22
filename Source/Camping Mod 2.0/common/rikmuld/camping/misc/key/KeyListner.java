package rikmuld.camping.misc.key;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.CampingMod;
import rikmuld.camping.client.gui.container.GuiContainerPlayerInv;
import rikmuld.camping.client.gui.screen.GuiScreenInvExtention;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.KeyInfo;

public class KeyListner implements IKeyListner{

	@Override
	public void keyUp(int key, EntityPlayer player)
	{
		
	}

	@Override
	public void keyDown(int key, EntityPlayer player) 	
	{
		if(player.worldObj.isRemote)
		{
			if(key==KeyInfo.getKey(KeyInfo.INV))player.openGui(CampingMod.instance, GuiInfo.GUI_INV_PLAYER, player.worldObj, 0, 0, 0);
		}
	}
}
