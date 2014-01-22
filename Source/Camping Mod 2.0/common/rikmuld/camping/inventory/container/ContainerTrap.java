package rikmuld.camping.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.entity.tileentity.TileEntityBearTrap;
import rikmuld.camping.inventory.slot.SlotItem;

public class ContainerTrap extends ContainerMain {

	private TileEntityBearTrap trap;

	public ContainerTrap(InventoryPlayer playerInv, IInventory tile)
	{
		trap = (TileEntityBearTrap)tile;

		addSlotToContainer(new SlotItem(tile, 0, 80, 12));

		ContainerUtil.addSlots(this, playerInv, 0, 1, 9, 8, 96);
		ContainerUtil.addSlots(this, playerInv, 9, 3, 9, 8, 38);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(slotNum);
		if((slot != null) && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(slotNum < trap.getSizeInventory())
			{
				if(!mergeItemStack(itemstack1, trap.getSizeInventory(), inventorySlots.size(), true)) return null;
			}
			else if(itemstack1.itemID > 4096? true:itemstack1.itemID < 256? false:itemstack1.itemID < 421)
			{
				if(!mergeItemStack(itemstack1, 0, 1, false)) return null;
			}
			else return null;

			if(itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}