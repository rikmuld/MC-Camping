package rikmuld.camping.entity.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileEntityInventory extends TileEntityMain implements IInventory, ISidedInventory {

	public ItemStack[] inventoryContents = new ItemStack[getSizeInventory()];

	@Override
	public boolean canExtractItem(int slot, ItemStack ItemStack, int side)
	{
		return false;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side)
	{
		return isItemValidForSlot(slot, itemStack);
	}

	@Override
	public void closeChest()
	{}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if(inventoryContents[slot] != null)
		{
			ItemStack itemstack;
			if(inventoryContents[slot].stackSize <= amount)
			{
				itemstack = inventoryContents[slot];
				inventoryContents[slot] = null;
				onInventoryChanged();
				return itemstack;
			}
			else
			{
				itemstack = inventoryContents[slot].splitStack(amount);
				if(inventoryContents[slot].stackSize == 0)
				{
					inventoryContents[slot] = null;
				}
				onInventoryChanged();
				return itemstack;
			}
		}
		else return null;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[0];
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public String getInvName()
	{
		return "container_" + getBlockType().getUnlocalizedName().substring(5);
	}

	@Override
	public abstract int getSizeInventory();

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventoryContents[slot];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if(inventoryContents[slot] != null)
		{
			ItemStack itemstack = inventoryContents[slot];
			inventoryContents[slot] = null;
			return itemstack;
		}
		else return null;
	}

	public boolean hasStackInSlot(int slot)
	{
		return getStackInSlot(slot) != null;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this? false:player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
	}

	@Override
	public void openChest()
	{}

	@Override
	public void readFromNBT(NBTTagCompound NBTTagCompound)
	{
		super.readFromNBT(NBTTagCompound);
		inventoryContents = new ItemStack[getSizeInventory()];
		NBTTagList inventory = NBTTagCompound.getTagList("Items");
		for(int i = 0; i < inventory.tagCount(); ++i)
		{
			NBTTagCompound Slots = (NBTTagCompound)inventory.tagAt(i);
			byte slot = Slots.getByte("Slot");
			if((slot >= 0) && (slot < inventoryContents.length))
			{
				inventoryContents[slot] = ItemStack.loadItemStackFromNBT(Slots);
			}
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inventoryContents[slot] = stack;
		if((stack != null) && (stack.stackSize > getInventoryStackLimit()))
		{
			stack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public void writeToNBT(NBTTagCompound NBTTagCompound)
	{
		super.writeToNBT(NBTTagCompound);
		NBTTagList inventory = new NBTTagList();
		for(int slot = 0; slot < inventoryContents.length; ++slot)
		{
			if(inventoryContents[slot] != null)
			{
				NBTTagCompound Slots = new NBTTagCompound();
				Slots.setByte("Slot", (byte)slot);
				inventoryContents[slot].writeToNBT(Slots);
				inventory.appendTag(Slots);
			}
		}
		NBTTagCompound.setTag("Items", inventory);
	}
}
