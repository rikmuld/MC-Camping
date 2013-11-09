package rikmuld.camping.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.entity.tileentity.TileEntityCampfireDeco;
import rikmuld.camping.inventory.slot.SlotItemsOnly;

public class ContainerCampfireDeco extends ContainerMain{

	private TileEntityCampfireDeco fire;
	private World worldObj;
	private ItemStack item = null;

	public ContainerCampfireDeco(InventoryPlayer playerInv, IInventory tile)
	{
		this.fire = (TileEntityCampfireDeco) tile;

		this.addSlotToContainer(new SlotItemsOnly(tile, 0, 71, 12, Item.dyePowder.itemID));
		
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
		Slot slot = (Slot) inventorySlots.get(slotNum);
		if(slot!=null&&slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(slotNum<fire.getSizeInventory())
			{
				if(!mergeItemStack(itemstack1, fire.getSizeInventory(), inventorySlots.size(), true))
				{
					return null;
				}
			}
			else
			{
				if(itemstack.getItem()==Item.dyePowder)
				{
					if(!mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else return null;
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
}