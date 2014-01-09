package rikmuld.camping.inventory.slot;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotItem extends Slot {

	public SlotItem(IInventory inv, int id, int x, int y)
	{
		super(inv, id, x, y);
	}

	public boolean isItemValid(ItemStack is)
	{
		return is.itemID>4096? true:is.itemID<256? false:is.itemID<421;
	}
}
