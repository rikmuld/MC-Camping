package rikmuld.camping.inventory.slot;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class SlotCooking extends Slot {

	public boolean active;
	Set<Integer> stacks;
	
	public SlotCooking(IInventory par1iInventory, int id, int x, int y)
	{
		super(par1iInventory, id, x, y);
		this.deActivate();
	}
	
	public void activate(int x, int y, Set<Integer> stacks)
	{
		this.active = true;
		this.stacks = stacks;
		
		this.xDisplayPosition = x;
		this.yDisplayPosition = y;
	}
	
	public void deActivate()
	{
		this.active = false;
		this.stacks = null;
		
		this.xDisplayPosition = -1000;
		this.yDisplayPosition = -1000;
	}
	
	@Override
    public boolean isItemValid(ItemStack stack)
    {
        return stacks!=null? stacks.contains(stack.itemID):false;
    }
	
	@Override
	public int getSlotStackLimit()
    {
        return 1;
    }
}
