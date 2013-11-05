package rikmuld.camping.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.item.IKnife;
import cpw.mods.fml.common.ICraftingHandler;

public class CraftHandler implements ICraftingHandler {

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		for(int slot = 0; slot<craftMatrix.getSizeInventory(); slot++)
		{
			if(craftMatrix.getStackInSlot(slot)!=null)
			{
				ItemStack itemInSlot = craftMatrix.getStackInSlot(slot);
				if(itemInSlot.getItem()!=null&&itemInSlot.getItem() instanceof IKnife)
				{
					ItemStack returnStack = ItemStackUtil.addDamage(itemInSlot, 1);
					if(returnStack!=null) returnStack.stackSize++;
					craftMatrix.setInventorySlotContents(slot, returnStack);
				}
			}
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item)
	{}
}
