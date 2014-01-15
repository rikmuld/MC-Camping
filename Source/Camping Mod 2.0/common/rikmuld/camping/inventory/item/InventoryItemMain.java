package rikmuld.camping.inventory.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryItemMain implements IInventory {

	public ItemStack[] inventoryContents;
	public NBTTagCompound tag;
	public String name;
	public ItemStack item;
	public int stackSize;

	public InventoryItemMain(ItemStack item, int size, int stacksize)
	{
		inventoryContents = new ItemStack[size];
		this.name = item.getUnlocalizedName();
		if(item.hasTagCompound()==false) item.setTagCompound(new NBTTagCompound());
		this.tag = item.getTagCompound();
		this.item = item;
		this.stackSize = stacksize;
	}

	@Override
	public int getSizeInventory()
	{
		return inventoryContents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.inventoryContents[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if(this.inventoryContents[slot]!=null)
		{
			ItemStack itemstack;
			if(this.inventoryContents[slot].stackSize<=amount)
			{
				itemstack = this.inventoryContents[slot];
				this.inventoryContents[slot] = null;
				this.onInventoryChanged();
				return itemstack;
			}
			else
			{
				itemstack = this.inventoryContents[slot].splitStack(amount);
				if(this.inventoryContents[slot].stackSize==0)
				{
					this.inventoryContents[slot] = null;
				}
				this.onInventoryChanged();
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public void onInventoryChanged()
	{
		this.writeToNBT(tag);
		this.setNBT(item);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if(this.inventoryContents[slot]!=null)
		{
			ItemStack itemstack = this.inventoryContents[slot];
			this.inventoryContents[slot] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.inventoryContents[slot] = stack;
		if(stack!=null&&stack.stackSize>this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
		this.onInventoryChanged();
	}

	@Override
	public String getInvName()
	{
		return "container_"+name;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return stackSize;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openChest()
	{
		this.readFromNBT(tag);
	}

	@Override
	public void closeChest()
	{
		this.writeToNBT(tag);
	}

	public void setNBT(ItemStack item)
	{
		item.setTagCompound(tag);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return true;
	}

	public void readFromNBT(NBTTagCompound NBTTagCompound)
	{
		this.inventoryContents = new ItemStack[this.getSizeInventory()];
		NBTTagList inventory = NBTTagCompound.getTagList("Items");
		for(int i = 0; i<inventory.tagCount(); ++i)
		{
			NBTTagCompound Slots = (NBTTagCompound) inventory.tagAt(i);
			byte slot = Slots.getByte("Slot");
			if(slot>=0&&slot<this.inventoryContents.length)
			{
				this.inventoryContents[slot] = ItemStack.loadItemStackFromNBT(Slots);
			}
		}
	}

	public void writeToNBT(NBTTagCompound NBTTagCompound)
	{
		NBTTagList inventory = new NBTTagList();
		for(int slot = 0; slot<this.inventoryContents.length; ++slot)
		{
			if(this.inventoryContents[slot]!=null)
			{
				NBTTagCompound Slots = new NBTTagCompound();
				Slots.setByte("Slot", (byte) slot);
				this.inventoryContents[slot].writeToNBT(Slots);
				inventory.appendTag(Slots);
			}
		}
		NBTTagCompound.setTag("Items", inventory);
	}
}