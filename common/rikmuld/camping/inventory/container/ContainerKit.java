package rikmuld.camping.inventory.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.inventory.item.InventoryItemMain;
import rikmuld.camping.inventory.slot.SlotNoPickup;
import rikmuld.camping.item.ItemKit;
import rikmuld.camping.misc.cooking.CookingEquipmentList;

public class ContainerKit extends ContainerMain {

	IInventory inv;
	
	public ContainerKit(InventoryPlayer playerInv, ItemStack stack)
	{
		inv = new InventoryItemMain(stack, 14, 1);

		ContainerUtil.addSlots(this, inv, 0, 1, 5, 44, 16);
		ContainerUtil.addSlots(this, inv, 5, 2, 1, 44, 34);
		ContainerUtil.addSlots(this, inv, 7, 2, 1, 116, 34);
		ContainerUtil.addSlots(this, inv, 9, 1, 5, 44, 70);

		ContainerUtil.addSlots(this, playerInv, 9, 3, 9, 8, 99);
		
		for(int row = 0; row<9; ++row)
		{
			if(row==playerInv.currentItem) this.addSlotToContainer(new SlotNoPickup(playerInv, row, 8+row*18, 157));
			else this.addSlotToContainer(new Slot(playerInv, row, 8+row*18, 157));
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
			else return null;
			
			if(itemstack1.stackSize==0)slot.putStack(null);
			else slot.onSlotChanged();		
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
		if(!player.worldObj.isRemote)
		{
			inv.closeChest();
			
			ArrayList<List<Integer>> items = new ArrayList<List<Integer>>();
			NBTTagList containingItems = (NBTTagList) ((InventoryItemMain)inv).tag.getTag("Items");
			
			for(int itemCound = 0; itemCound<containingItems.tagCount(); itemCound++)
			{
				ItemStack item = ItemStack.loadItemStackFromNBT((NBTTagCompound) containingItems.tagAt(itemCound));
				items.add(Arrays.asList(item.itemID, item.getItemDamage()));
			}
			
			if(CookingEquipmentList.getCookingForRecipe(items)!=null)ItemStackUtil.setCurrentPlayerItem(player, CookingEquipmentList.getCookingForRecipe(items).itemInfo);
			else if(items.size()>0)ItemStackUtil.setCurrentPlayerItem(player, new ItemStack(ModItems.kit, 1, ItemKit.KIT_USELESS));
			else ItemStackUtil.setCurrentPlayerItem(player, new ItemStack(ModItems.kit, 1, ItemKit.KIT_EMPTY));
				
			((InventoryItemMain) inv).setNBT(player.getCurrentEquippedItem());
		}
	}
}
