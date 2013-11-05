package rikmuld.camping.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.inventory.item.InventoryItemMain;
import rikmuld.camping.inventory.slot.SlotNoPickup;

public class ContainerBackpack extends ContainerMain {

	IInventory inv;
	
	public ContainerBackpack(InventoryPlayer playerInv, ItemStack stack)
	{
		inv = new InventoryItemMain(stack, 27, 64);

		ContainerUtil.addSlots(this, inv, 0, 3, 9, 8, 26);
		ContainerUtil.addSlots(this, playerInv, 9, 3, 9, 8, 84);
		
		for(int row = 0; row<9; ++row)
		{
			if(row==playerInv.currentItem) this.addSlotToContainer(new SlotNoPickup(playerInv, row, 8+row*18, 142));
			else this.addSlotToContainer(new Slot(playerInv, row, 8+row*18, 142));
		}
		inv.openChest();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if(slot!=null&&slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(i<inv.getSizeInventory())
			{
				if(!mergeItemStack(itemstack1, inv.getSizeInventory(), inventorySlots.size(), true))
				{
					return null;
				}
			}
			else
			{
				if(!mergeItemStack(itemstack1, 0, inv.getSizeInventory(), false))
				{
					return null;
				}
			}
			if(itemstack1.stackSize==0)
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

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return !player.isDead;
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		inv.closeChest();
		((InventoryItemMain) inv).setNBT(player.getCurrentEquippedItem());
	}
}
