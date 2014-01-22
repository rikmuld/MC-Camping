package rikmuld.camping.misc.key;

import net.minecraft.entity.player.EntityPlayer;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;
import rikmuld.camping.core.lib.KeyInfo;

public class KeyListner implements IKeyListner {

	@Override
	public void keyDown(int key, EntityPlayer player)
	{
		if(player.worldObj.isRemote)
		{
			if(key == KeyInfo.getKey(KeyInfo.INV))
			{
				player.openGui(CampingMod.instance, GuiInfo.GUI_INV_PLAYER, player.worldObj, 0, 0, 0);
			}
		}
	}

	@Override
	public void keyUp(int key, EntityPlayer player)
	{

	}
}
