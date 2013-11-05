package rikmuld.camping.inventory.container;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.inventory.item.InventoryItemMain;
import rikmuld.camping.inventory.player.InventoryCampingInvBack;
import rikmuld.camping.inventory.slot.SlotDisable;
import rikmuld.camping.inventory.slot.SlotItemsOnly;

public class ContainerCampinvBack extends ContainerMain {

	IInventory inventory;
	IInventory backpack;

	ArrayList<SlotDisable> slots = new ArrayList<SlotDisable>();
	
	public ContainerCampinvBack(EntityPlayer player)
	{
		backpack = new InventoryItemMain(new ItemStack(ModItems.backpack.itemID, 1, 0), 27, 64);
		
		for(int row = 0; row<3; ++row)
		{
			for(int collom = 0; collom<9; ++collom)
			{
				SlotDisable slot = new SlotDisable(backpack, collom+row*9, 8+collom*18, 26+row*18);
				slot.disable();
				this.addSlotToContainer(slot);
				slots.add(slot);
			}
		}

		inventory = new InventoryCampingInvBack(player, 1, slots, (InventoryItemMain) backpack);
		this.addSlotToContainer(new SlotItemsOnly(inventory, 0, 80, 6, ModItems.backpack.itemID));

		ContainerUtil.addSlots(this, player.inventory, 9, 3, 9, 8, 84);
		ContainerUtil.addSlots(this, player.inventory, 0, 1, 9, 8, 142);
		
		inventory.openChest();
		backpack.openChest();
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
			
			if(i<28)
			{
				if(!mergeItemStack(itemstack1, 28, inventorySlots.size(), true))
				{
					return null;
				}
			}
			else
			{
				if(inventory.getStackInSlot(0)==null)
				{
					if(itemstack1.itemID!=ModItems.backpack.itemID||!mergeItemStack(itemstack1, backpack.getSizeInventory(), backpack.getSizeInventory()+inventory.getSizeInventory(), false))
					{
						return null;
					}
				}
				else
				{
					if(!mergeItemStack(itemstack1, 0, backpack.getSizeInventory(), false))
					{
						return null;
					}
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
		return true;
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		inventory.closeChest();
		backpack.closeChest();
	}
}
