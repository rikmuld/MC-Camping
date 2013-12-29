package rikmuld.camping.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModItems;

public class ItemFoodMarshmallow extends ItemFoodMain{

	public ItemFoodMarshmallow(String name)
	{
		super(name, 4, 0.2F, false);
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
