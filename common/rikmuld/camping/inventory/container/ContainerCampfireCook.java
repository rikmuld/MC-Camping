package rikmuld.camping.inventory.container;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.inventory.slot.SlotCooking;
import rikmuld.camping.inventory.slot.SlotItemsOnly;
import rikmuld.camping.misc.cooking.CookingEquipmentList;
import rikmuld.camping.tileentity.TileEntityCampfireCook;

public class ContainerCampfireCook extends ContainerMain{

	private TileEntityCampfireCook fire;
	private World worldObj;
	public ItemStack[] stacks = new ItemStack[10];
	
	public ContainerCampfireCook(InventoryPlayer playerInv, IInventory tile)
	{
		ArrayList<SlotCooking> slots = new ArrayList<SlotCooking>();
		
		this.fire = (TileEntityCampfireCook) tile;

		this.addSlotToContainer(new SlotItemsOnly(tile, 0, 80, 84, Item.coal.itemID));
		this.addSlotToContainer(new SlotItemsOnly(tile, 1, 150, 9, ModItems.kit.itemID));

		for(int i = 0; i<10; i++)
		{
			SlotCooking slot = new SlotCooking(tile, i+2, 0, 0);
			slots.add(slot);
			this.addSlotToContainer(slot);
		}
		
		fire.setSlots(slots);
		
		ContainerUtil.addSlots(this, playerInv, 0, 1, 9, 8, 164);
		ContainerUtil.addSlots(this, playerInv, 9, 3, 9, 8, 106);
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
				if(itemstack.getItem()==Item.coal)
				{
					if(!mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if(CookingEquipmentList.equipment.keySet().contains(Arrays.asList(itemstack.itemID, itemstack.getItemDamage())))
				{
					if(!mergeItemStack(itemstack1, 1, 2, false))
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