package rikmuld.camping.inventory.player;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.inventory.item.InventoryItemMain;
import rikmuld.camping.inventory.slot.SlotDisable;

public class InventoryCampingInvBack extends InventoryPlayerMain {

	ArrayList<SlotDisable> slots;

	InventoryItemMain backpack;

	public InventoryCampingInvBack(EntityPlayer player, int size, ArrayList<SlotDisable> slots, InventoryItemMain backpack)
	{
		super(player, size);
		this.slots = slots;
		this.backpack = backpack;

		if(player.getEntityData().hasKey("campInv") == false)
		{
			player.getEntityData().setCompoundTag("campInv", new NBTTagCompound());
		}
		if(player.getEntityData().getCompoundTag("campInv").hasKey("backpack") == false)
		{
			player.getEntityData().getCompoundTag("campInv").setCompoundTag("backpack", new NBTTagCompound());
		}
		tag = player.getEntityData().getCompoundTag("campInv").getCompoundTag("backpack");
	}

	public static void dropItems(EntityPlayer player)
	{
		if(player.getEntityData().hasKey("campInv") == false) return;
		if(player.getEntityData().getCompoundTag("campInv").hasKey("backpack") == false) return;

		NBTTagCompound tag = player.getEntityData().getCompoundTag("campInv").getCompoundTag("backpack");
		NBTTagList inventory = tag.getTagList("Items");
		for(int i = 0; i < inventory.tagCount(); ++i)
		{
			NBTTagCompound Slots = (NBTTagCompound)inventory.tagAt(i);
			Slots.getByte("Slot");
			ItemStackUtil.dropItemsInWorld(new ItemStack[]{ItemStack.loadItemStackFromNBT(Slots)}, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	}

	@Override
	public void onInventoryChanged()
	{
		writeToNBT(tag);
		if(getStackInSlot(0) != null)
		{
			for(SlotDisable slot: slots)
			{
				slot.enable();
				if(!getStackInSlot(0).hasTagCompound())
				{
					getStackInSlot(0).setTagCompound(new NBTTagCompound());
				}
				backpack.tag = getStackInSlot(0).getTagCompound();
				backpack.readFromNBT(backpack.tag);
			}
			backpack.item = getStackInSlot(0);
		}
		else
		{
			for(SlotDisable slot: slots)
			{
				slot.disable();
				backpack.tag = new NBTTagCompound();
				backpack.readFromNBT(backpack.tag);
			}
		}
	}

	@Override
	public void openChest()
	{
		super.openChest();
		onInventoryChanged();
	}
}