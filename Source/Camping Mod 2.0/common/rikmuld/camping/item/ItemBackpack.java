package rikmuld.camping.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.CampingMod;
import rikmuld.camping.core.lib.GuiInfo;

public class ItemBackpack extends ItemMain {

	public ItemBackpack(String name)
	{
		super(name);
		maxStackSize = 1;
		setHasSubtypes(true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			player.openGui(CampingMod.instance, GuiInfo.GUI_BACKPACK, world, 0, 0, 0);
		}
		return stack;
	}
}
