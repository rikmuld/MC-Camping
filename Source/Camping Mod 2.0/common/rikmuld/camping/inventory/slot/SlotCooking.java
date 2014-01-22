package rikmuld.camping.inventory.slot;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCooking extends Slot {

	public boolean active;
	Set<List<Integer>> stacks;

	public SlotCooking(IInventory par1iInventory, int id, int x, int y)
	{
		super(par1iInventory, id, x, y);
		deActivate();
	}

	public void activate(int x, int y, Set<List<Integer>> stacks)
	{
		active = true;
		this.stacks = stacks;

		xDisplayPosition = x;
		yDisplayPosition = y;
	}

	public void deActivate()
	{
		active = false;
		stacks = null;

		xDisplayPosition = -1000;
		yDisplayPosition = -1000;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stacks != null? stacks.contains(Arrays.asList(stack.itemID, stack.getItemDamage())):false;
	}
}
