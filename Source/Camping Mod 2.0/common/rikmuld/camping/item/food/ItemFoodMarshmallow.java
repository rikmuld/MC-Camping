package rikmuld.camping.item.food;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.core.lib.ConfigInfo;
import rikmuld.camping.core.lib.ConfigInfo.ConfigInfoInteger;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.item.ItemParts;

public class ItemFoodMarshmallow extends ItemFoodMain {

	public ItemFoodMarshmallow(String name)
	{
		super(name, ConfigInfoInteger.value(ConfigInfo.HEAL_MARSH), 0.2F, false);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
	{
		super.onFoodEaten(stack, world, player);

		if(!world.isRemote)
		{
			if(!player.inventory.addItemStackToInventory(new ItemStack(ModItems.parts, 1, ItemParts.STICK)))
			{
				player.dropPlayerItem(new ItemStack(ModItems.parts, 1, ItemParts.STICK));
			}
		}
	}
}
