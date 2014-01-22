package rikmuld.camping.inventory.slot;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotItemsOnly extends SlotDisable {

	ArrayList<Integer> ID = new ArrayList<Integer>();

	public SlotItemsOnly(IInventory inventory, int slotIndex, int xPos, int yPos, int... IDs)
	{
		super(inventory, slotIndex, xPos, yPos);
		for(Integer id: IDs)
		{
			ID.add(id);
		}
	}

	public SlotItemsOnly(IInventory tile, int slotIndex, int xPos, int yPos, Set<Integer> keySet)
	{
		this(tile, slotIndex, xPos, yPos);
		for(Integer id: keySet)
		{
			ID.add(id);
		}
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		return ID.contains(-1)? true:ID.contains(is.itemID);
	}
}
