package rikmuld.camping.inventory.container;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.entity.tileentity.TileEntityTent;
import rikmuld.camping.inventory.slot.SlotCooking;
import rikmuld.camping.inventory.slot.SlotItemsOnly;
import rikmuld.camping.misc.cooking.CookingEquipmentList;

public class ContainerTentLanterns extends ContainerMain{

	private TileEntityTent tent;
	private World worldObj;
	
	public ContainerTentLanterns(InventoryPlayer playerInv, IInventory tile)
	{		
		this.tent = (TileEntityTent) tile;
		this.worldObj = tent.worldObj;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
	{	
		return null;
	}
}