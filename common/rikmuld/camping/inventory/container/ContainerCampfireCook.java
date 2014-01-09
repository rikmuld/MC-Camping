package rikmuld.camping.inventory.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.inventory.slot.SlotAchivementGet;
import rikmuld.camping.inventory.slot.SlotCooking;
import rikmuld.camping.inventory.slot.SlotCookingAchievementGet;
import rikmuld.camping.inventory.slot.SlotItemsOnly;
import rikmuld.camping.misc.cooking.CookingEquipmentList;

public class ContainerCampfireCook extends ContainerMain{

	private int[] cookProgress = new int[10];
	private TileEntityCampfireCook fire;
	private World worldObj;
	
	public ContainerCampfireCook(InventoryPlayer playerInv, IInventory tile)
	{
		ArrayList<SlotCooking> slots = new ArrayList<SlotCooking>();
		
		this.fire = (TileEntityCampfireCook) tile;

		this.addSlotToContainer(new SlotAchivementGet(tile, 0, 80, 84, playerInv.player, ModAchievements.campfireCook, Item.coal.itemID));
		this.addSlotToContainer(new SlotCookingAchievementGet(tile, 1, 150, 9, playerInv.player, ModItems.kit.itemID));

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
	
	public void addCraftingToCrafters(ICrafting crafting)
	{
		super.addCraftingToCrafters(crafting);
		for(int i = 0 ; i<fire.cookProgress.length; i++)crafting.sendProgressBarUpdate(this, i, this.fire.cookProgress[i]);
	}

	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		Iterator<?> var1 = this.crafters.iterator();

		while(var1.hasNext())
		{
			ICrafting var2 = (ICrafting) var1.next();

			for(int i = 0 ; i<fire.cookProgress.length; i++)
			{
				if(this.cookProgress[i]!=this.fire.cookProgress[i])
				{
					var2.sendProgressBarUpdate(this, i, this.fire.cookProgress[i]);
				}
			}
		}

		for(int i = 0 ; i<fire.cookProgress.length; i++)this.cookProgress[i] = this.fire.cookProgress[i];
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		this.fire.cookProgress[id] = data;
	}
}