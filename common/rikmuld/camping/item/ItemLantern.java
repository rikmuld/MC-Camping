package rikmuld.camping.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemLantern extends ItemMain {

	public ItemLantern(String name)
	{
		super(name);
		this.setHasSubtypes(true);
		this.setMaxDamage(600);
		this.setMaxStackSize(1);
		this.canRepair = false;
	}
	
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
    	list.add("Burning time left: "+(stack.getMaxDamage()-stack.getItemDamage())/2+" seconds");
    }
}
