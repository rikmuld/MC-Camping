package rikmuld.camping.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileEntityInventory extends TileEntityMain implements IInventory, ISidedInventory {

	public ItemStack[] inventoryContents = new ItemStack[this.getSizeInventory()];

	@Override
	public abstract int getSizeInventory();

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
		super.onInventoryChanged();
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

	public boolean hasStackInSlot(int slot)
	{
		return this.getStackInSlot(slot)!=null;
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
		return "container_"+this.getBlockType().getUnlocalizedName().substring(5);
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord)!=this ? false : player.getDistanceSq((double) this.xCoord+0.5D, (double) this.yCoord+0.5D,
				(double) this.zCoord+0.5D)<=64.0D;
	}

	@Override
	public void openChest()
	{}

	@Override
	public void closeChest()
	{}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side)
	{
		return this.isItemValidForSlot(slot, itemStack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack ItemStack, int side)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[0];
	}

	@Override
	public void readFromNBT(NBTTagCompound NBTTagCompound)
	{
		super.readFromNBT(NBTTagCompound);
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

	@Override
	public void writeToNBT(NBTTagCompound NBTTagCompound)
	{
		super.writeToNBT(NBTTagCompound);
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
