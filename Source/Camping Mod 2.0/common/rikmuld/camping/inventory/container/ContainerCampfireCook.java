package rikmuld.camping.inventory.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.inventory.slot.SlotAchivementGet;
import rikmuld.camping.inventory.slot.SlotCooking;
import rikmuld.camping.inventory.slot.SlotCookingAchievementGet;
import rikmuld.camping.inventory.slot.SlotItemsOnly;
import rikmuld.camping.misc.cooking.CookingEquipmentList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCampfireCook extends ContainerMain {

	private TileEntityCampfireCook fire;

	public ContainerCampfireCook(InventoryPlayer playerInv, IInventory tile)
	{
		ArrayList<SlotCooking> slots = new ArrayList<SlotCooking>();

		fire = (TileEntityCampfireCook)tile;

		addSlotToContainer(new SlotAchivementGet(tile, 0, 80, 84, playerInv.player, ModAchievements.campfireCook, Item.coal.itemID));
		addSlotToContainer(new SlotCookingAchievementGet(tile, 1, 150, 9, playerInv.player, ModItems.kit.itemID));

		for(int i = 0; i < 10; i++)
		{
			SlotCooking slot = new SlotCooking(tile, i + 2, 0, 0);
			slots.add(slot);
			addSlotToContainer(slot);
		}
		
		SlotItemsOnly slot = new SlotItemsOnly(tile, 12, 8, 8, Item.bowlEmpty.itemID);
		slot.disable();
		addSlotToContainer(slot);

		fire.setSlots(slots);
		fire.bowlSlot = slot;
		
		if(playerInv.player.worldObj.isRemote)fire.manageBowlSlot();

		ContainerUtil.addSlots(this, playerInv, 0, 1, 9, 8, 164);
		ContainerUtil.addSlots(this, playerInv, 9, 3, 9, 8, 106);
	}

	@Override
	public void addCraftingToCrafters(ICrafting crafting)
	{
		super.addCraftingToCrafters(crafting);
		for(int i = 0; i < fire.cookProgress.length; i++)
		{
			crafting.sendProgressBarUpdate(this, i, fire.cookProgress[i]);
		}
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
			if(slotNum < fire.getSizeInventory())
			{
				if(!mergeItemStack(itemstack1, fire.getSizeInventory(), inventorySlots.size(), true)) return null;
			}
			else
			{
				if(itemstack.getItem() == Item.coal)
				{
					if(!mergeItemStack(itemstack1, 0, 1, false)) return null;
				}
				else if(CookingEquipmentList.equipment.keySet().contains(Arrays.asList(itemstack.itemID, itemstack.getItemDamage())))
				{
					if(!mergeItemStack(itemstack1, 1, 2, false)) return null;
				}
				else return null;
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