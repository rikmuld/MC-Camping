package rikmuld.camping.misc.creativeTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;

public class TabMain extends CreativeTabs {

	public TabMain(String label)
	{
		super(label);
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(ModBlocks.campfireDeco);
	}
}
