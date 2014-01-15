package rikmuld.camping.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.inventory.slot.SlotItemsOnly;

public class ContainerTentLanterns extends ContainerMain{

	private TileEntityTent tent;
	private World worldObj;
	
	public ContainerTentLanterns(InventoryPlayer playerInv, IInventory tile)
	{		
		this.tent = (TileEntityTent) tile;
		this.worldObj = tent.worldObj;
		
		this.addSlotToContainer(new SlotItemsOnly(tile, 0, 80, 88, Item.glowstone.itemID));
		
		ContainerUtil.addSlots(this, playerInv, 9, 3, 9, 8, 113);
		ContainerUtil.addSlots(this, playerInv, 0, 1, 9, 8, 171);
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
		Slot slot = (Slot) inventorySlots.get(i);
		if(slot!=null&&slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(i<tent.getSizeInventory())
			{
				if(!mergeItemStack(itemstack1, tent.getSizeInventory(), inventorySlots.size(), true))return null;
			}
			else if(itemstack1.itemID == Item.glowstone.itemID)
			{
				if(!mergeItemStack(itemstack1, 0,  tent.getSizeInventory(), true))return null;
			}
			
			if(itemstack1.stackSize==0)slot.putStack(null);
			else slot.onSlotChanged();		
		}
		return itemstack;
	}
}