package rikmuld.camping.misc.key;

import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.KeyInfo;

public class KeyListner implements IKeyListner {

	@Override
	public void keyDown(String key, EntityPlayer player)
	{
		if(player.worldObj.isRemote)
		{
			if(key.equals(KeyInfo.INV))
			{
				if(!player.capabilities.isCreativeMode)
				{
					player.openGui(CampingMod.instance, GuiInfo.GUI_INV_PLAYER, player.worldObj, 0, 0, 0);
				}
				else
				{
					player.openGui(CampingMod.instance, GuiInfo.GUI_INV_CREATIVE, player.worldObj, 0, 0, 0);
				}
			}
		}
	}

	@Override
	public void keyUp(String key, EntityPlayer player)
	{

	}
}
