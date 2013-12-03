package rikmuld.camping.core.util;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackUtil {

	static Random rand = new Random();

	public static ItemStack[] getMetaCycle(Object input, int maxMetadata)
	{
		ItemStack[] stack = new ItemStack[maxMetadata];
		for(int i = 0; i<maxMetadata; i++)
		{
			if(input instanceof Block) stack[i] = new ItemStack((Block) input, 1, i);
			if(input instanceof Item) stack[i] = new ItemStack((Item) input, 1, i);
		}
		return stack;
	}

	public static ItemStack getWildValue(ItemStack input)
	{
		return new ItemStack(input.itemID, 1, OreDictionary.WILDCARD_VALUE);
	}

	public static void setCurrentPlayerItem(EntityPlayer player, ItemStack stack)
	{
		player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
	}
	
	public static void addDamage(ItemStack item, EntityPlayer player, int damage)
	{
		ItemStack returnStack = new ItemStack(item.itemID, 1, (item.getItemDamage()+damage));
		player.inventory.setInventorySlotContents(player.inventory.currentItem, (returnStack.getItemDamage()>=item.getMaxDamage()) ? null : returnStack);
	}

	public static ItemStack addDamage(ItemStack item, int damage)
	{
		ItemStack returnStack = new ItemStack(item.itemID, 1, (item.getItemDamage()+damage));
		ItemStack returnStack2 = returnStack.copy();
		returnStack2.stackSize = 0;
		return returnStack.getItemDamage()>=item.getMaxDamage() ? returnStack2 : returnStack;
	}

	public static void dropItemsInWorld(ItemStack[] stacks, World world, int x, int y, int z)
	{
		if(!world.isRemote)
		{
			for(int i = 0; i<stacks.length; i++)
			{
				ItemStack itemStack = stacks[i];
				dropItemInWorld(itemStack, world, x, y, z);
			}
		}
	}
	
	public static void dropItemsInWorld(ArrayList<ItemStack> stacks, World world, int x, int y, int z)
	{
		if(!world.isRemote)
		{
			for(int i = 0; i<stacks.size(); i++)
			{
				ItemStack itemStack = stacks.get(i);
				dropItemInWorld(itemStack, world, x, y, z);
			}
		}
	}
	
	public static void dropItemInWorld(ItemStack itemStack, World world, int x, int y, int z)
	{
		if(!world.isRemote)
		{
			if(itemStack!=null&&itemStack.stackSize>0)
			{
				float dX = rand.nextFloat()*0.8F+0.1F;
				float dY = rand.nextFloat()*0.8F+0.1F;
				float dZ = rand.nextFloat()*0.8F+0.1F;
				EntityItem entityItem = new EntityItem(world, x+dX, y+dY, z+dZ, new ItemStack(itemStack.itemID, itemStack.stackSize, itemStack.getItemDamage()));
				if(itemStack.hasTagCompound())
				{ 
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
				}
				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian()*factor;
				entityItem.motionY = rand.nextGaussian()*factor+0.2F;
				entityItem.motionZ = rand.nextGaussian()*factor;
				world.spawnEntityInWorld(entityItem);
				itemStack.stackSize = 0;
			}
		}
	}
	
	public static boolean canMergePartStacks(ItemStack stack, ItemStack stack2)
	{
		if(stack.itemID==stack2.itemID&&stack.getItemDamage()==stack2.getItemDamage()&&stack.stackSize<stack.getMaxStackSize())
		{
			if(stack.hasTagCompound()==stack2.hasTagCompound())
			{
				if(stack.hasTagCompound()&&stack.getTagCompound().equals(stack2.getTagCompound()))return true;
				else if(!stack.hasTagCompound())return true;
			}
		}
		return false;
	}
}
