package rikmuld.camping.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.inventory.slot.SlotState;

public class ContainerTentChests extends ContainerMain {

	private TileEntityTent tent;
	SlotState[][] slots = new SlotState[25][6];

	public ContainerTentChests(InventoryPlayer playerInv, IInventory tile)
	{
		tent = (TileEntityTent)tile;
		for(int collom = 0; collom < 25; ++collom)
		{
			for(int row = 0; row < 6; ++row)
			{
				SlotState slot = new SlotState(tile, row + (collom * 6) + 1, 9 + (collom * 18), 34 + (row * 18));
				addSlot(slot);
				slot.disable();
				slots[collom][row] = slot;
			}
		}

		ContainerUtil.addSlots(this, playerInv, 9, 3, 9, 27, 146);
		ContainerUtil.addSlots(this, playerInv, 0, 1, 9, 27, 204);

		tent.setSlots(slots);
		tent.manageSlots();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p, int i)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(i);
		if((slot != null) && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(i < tent.getSizeInventory())
			{
				if(!mergeItemStack(itemstack1, tent.getSizeInventory() - 1, inventorySlots.size(), true)) return null;
			}
			else
			{
				if(!mergeItemStack(itemstack1, 0, tent.chests * 5 * 6, false)) return null;
			}

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