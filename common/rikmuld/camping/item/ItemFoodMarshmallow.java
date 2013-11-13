package rikmuld.camping.item;

import rikmuld.camping.core.register.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFoodMarshmallow extends ItemFoodMain{

	public ItemFoodMarshmallow(String name)
	{
		super(name, 4, 0.1F, false);
		this.setAlwaysEdible();
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
