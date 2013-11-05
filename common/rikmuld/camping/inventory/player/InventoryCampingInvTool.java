package rikmuld.camping.inventory.player;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.inventory.container.ContainerCampinvTool;
import rikmuld.camping.inventory.item.InventoryItemMain;
import rikmuld.camping.inventory.slot.SlotDisable;

public class InventoryCampingInvTool extends InventoryPlayerMain{

	ArrayList<SlotDisable> slots;
	Container container;
	EntityPlayer player;
	
	public InventoryCampingInvTool(Container container, EntityPlayer player, int size, ArrayList<SlotDisable> slots)
	{
		super(player, size);
		this.slots = slots;
		this.container = container;
		this.player = player;
		
		if(player.getEntityData().hasKey("campInv")==false) player.getEntityData().setCompoundTag("campInv", new NBTTagCompound());
		if(player.getEntityData().getCompoundTag("campInv").hasKey("tool")==false) player.getEntityData().getCompoundTag("campInv").setCompoundTag("tool", new NBTTagCompound());
		this.tag = player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool");
	}
	
	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		
		if(this.hasItemInToolsSlot(ModItems.knife.itemID))for(SlotDisable slot:slots)slot.enable();
		else for(SlotDisable slot:slots)
		{
			container.onContainerClosed(player);
			slot.disable();
		}
	}
	
	public boolean hasItemInToolsSlot(int id)
	{
		for(int i = 0; i<3; i++)
		{
			if(this.getStackInSlot(i)!=null&&this.getStackInSlot(i).itemID==id)return true;
		}
		return false;
	}
	
	@Override
	public void openChest()
	{
		super.openChest();
		this.onInventoryChanged();
	}
	
	public static void dropItems(EntityPlayer player)
	{
		if(player.getEntityData().hasKey("campInv")==false) return;
		if(player.getEntityData().getCompoundTag("campInv").hasKey("tool")==false) return;
		
		NBTTagCompound tag = player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool");
		NBTTagList inventory = tag.getTagList("Items");
		for(int i = 0; i<inventory.tagCount(); ++i)
		{
			NBTTagCompound Slots = (NBTTagCompound) inventory.tagAt(i);
			byte slot = Slots.getByte("Slot");
			ItemStackUtil.dropItemsInWorld(new ItemStack[]{ItemStack.loadItemStackFromNBT(Slots)}, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	}
}