package rikmuld.camping.misc.key;

import net.minecraft.entity.player.EntityPlayer;

public interface IKeyListner {

	public void keyDown(String key, EntityPlayer player);

	public void keyUp(String key, EntityPlayer player);
}
