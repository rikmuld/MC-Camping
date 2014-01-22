package rikmuld.camping.core.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerUtil {

	public static NBTTagCompound getPlayerDataTag(EntityPlayer player)
	{
		if(!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
		{
			player.getEntityData().setCompoundTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		}
		return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
	}
}
