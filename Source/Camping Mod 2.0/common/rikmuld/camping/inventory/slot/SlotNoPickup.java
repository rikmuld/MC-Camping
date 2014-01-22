package rikmuld.camping.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotNoPickup extends Slot {

	public SlotNoPickup(IInventory inventory, int slotIndex, int xPos, int yPos)
	{
		super(inventory, slotIndex, xPos, yPos);
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer)
	{
		return false;
	}
}
