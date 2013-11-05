package rikmuld.camping.core.util;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.MapData;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.item.ItemParts;
import rikmuld.camping.network.PacketTypeHandler;
import rikmuld.camping.network.packets.PacketPlayerData;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class CampingInvUtil {

	public static boolean hasLantarn(EntityPlayer player)
	{
		return loadItemsFromNBT(player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool")).contains(ModItems.lantern.itemID);
	}
	
	public static boolean hasMap(EntityPlayer player)
	{
		return loadItemsFromNBT(player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool")).contains(Item.map.itemID);
	}
	
	public static MapData getMapData(EntityPlayer player)
	{
		ArrayList<ItemStack> stacks = loadItemsStacksFromNBT(player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool"));
		
		for(int i = 0; i<stacks.size(); i++)
		{
			if(stacks.get(i).itemID==Item.map.itemID)
			{
				return Item.map.getMapData(stacks.get(i), player.worldObj);
			}
		}
		return null;
	}
	
	public static ArrayList<Integer> loadItemsFromNBT(NBTTagCompound tag)
	{
		ArrayList<Integer> stacks = new ArrayList<Integer>();
		
		NBTTagList inventory = tag.getTagList("Items");
		for(int i = 0; i<inventory.tagCount(); ++i)
		{
			NBTTagCompound Slots = (NBTTagCompound) inventory.tagAt(i);
			byte slot = Slots.getByte("Slot");
			stacks.add(ItemStack.loadItemStackFromNBT(Slots).itemID);
		}
		
		return stacks;
	}
	
	public static ArrayList<ItemStack> loadItemsStacksFromNBT(NBTTagCompound tag)
	{
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		
		NBTTagList inventory = tag.getTagList("Items");
		for(int i = 0; i<inventory.tagCount(); ++i)
		{
			NBTTagCompound Slots = (NBTTagCompound) inventory.tagAt(i);
			byte slot = Slots.getByte("Slot");
			stacks.add(ItemStack.loadItemStackFromNBT(Slots));
		}
		
		return stacks;
	}
	
	public static void lanternTick(EntityPlayer player)
	{
		ArrayList<ItemStack> stacks = loadItemsStacksFromNBT(player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool"));
		ArrayList<ItemStack> stacks2 = new ArrayList<ItemStack>();
		for(ItemStack stack: stacks)
		{
			if(stack.itemID==ModItems.lantern.itemID)
			{
				if(stack.getItemDamage()<stack.getMaxDamage()-1)
				{
					stack=ItemStackUtil.addDamage(stack, 1);
				}
				else stack = new ItemStack(ModItems.parts, 1, ItemParts.LANTERN_OFF);
			}
			stacks2.add(stack);
		}
		player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool").setTag("Items", getNBT(stacks2));
	}
	
	public static NBTTagList getNBT(ArrayList<ItemStack> stacks)
	{
		NBTTagList inventory = new NBTTagList();
		for(int slot = 0; slot<stacks.size(); ++slot)
		{
			if(stacks.get(slot)!=null)
			{
				NBTTagCompound Slots = new NBTTagCompound();
				Slots.setByte("Slot", (byte) slot);
				stacks.get(slot).writeToNBT(Slots);
				inventory.appendTag(Slots);
			}
		}
		return inventory;
	}
	
	public static int getLanternDamage(EntityPlayer player)
	{
		ArrayList<ItemStack> stacks = loadItemsStacksFromNBT(player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool"));
		for(ItemStack stack: stacks)
		{
			if(stack.itemID==ModItems.lantern.itemID)
			{
				return stack.getItemDamage();
			}
		}
		return -1;
	}
}
