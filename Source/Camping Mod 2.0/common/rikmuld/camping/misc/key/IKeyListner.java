package rikmuld.camping.misc.key;

import net.minecraft.entity.player.EntityPlayer;

public interface IKeyListner {

	public void keyUp(int key, EntityPlayer player);

	public void keyDown(int key, EntityPlayer player);
}
