package rikmuld.camping.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ItemStackUtil;
import rikmuld.camping.item.IKnife;
import rikmuld.camping.item.ItemParts;
import rikmuld.camping.item.itemblock.ItemBlockLantern;
import cpw.mods.fml.common.ICraftingHandler;

public class CraftHandler implements ICraftingHandler {

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		for(int slot = 0; slot<craftMatrix.getSizeInventory(); slot++)
		{
			if(craftMatrix.getStackInSlot(slot)!=null)
			{
				ItemStack itemInSlot = craftMatrix.getStackInSlot(slot);
				if(itemInSlot.getItem()!=null&&itemInSlot.getItem() instanceof IKnife)
				{
					ItemStack returnStack = ItemStackUtil.addDamage(itemInSlot, 1);
					if(returnStack!=null) returnStack.stackSize++;
					craftMatrix.setInventorySlotContents(slot, returnStack);
				}
				if(itemInSlot.getItem()!=null&&itemInSlot.itemID == Item.bowlEmpty.itemID&&item.itemID==ModItems.parts.itemID&&item.getItemDamage()==ItemParts.MARSHMALLOW)
				{
					ItemStack returnStack = new ItemStack(Item.bowlEmpty.itemID, itemInSlot.stackSize+1, 0);
					craftMatrix.setInventorySlotContents(slot, returnStack);
				}
				if(itemInSlot.getItem()!=null&&itemInSlot.itemID == Item.potion.itemID&&item.itemID==ModItems.parts.itemID&&item.getItemDamage()==ItemParts.MARSHMALLOW)
				{
					if(!player.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle)))
					{
						player.dropPlayerItem(new ItemStack(Item.glassBottle));
					}
				}
				if(item.itemID==ModBlocks.lantern.blockID&&item.getItemDamage()==ItemBlockLantern.LANTERN_ON)
				{
					item.setTagCompound(new NBTTagCompound());
					item.getTagCompound().setInteger("time", 1500);
				}
				if(item.itemID==ModBlocks.tent.blockID)
				{
					if(craftMatrix.getStackInSlot(slot).itemID==Item.dyePowder.itemID)
					{
						item.setTagCompound(new NBTTagCompound());
						item.getTagCompound().setInteger("color", craftMatrix.getStackInSlot(slot).getItemDamage());
					}
				}
			}
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item)
	{}
}
