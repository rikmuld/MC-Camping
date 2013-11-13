package rikmuld.camping.inventory.container;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import rikmuld.camping.core.register.ModBlocks;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.util.ContainerUtil;
import rikmuld.camping.core.util.CampingInvUtil;
import rikmuld.camping.inventory.player.InventoryCampingInvTool;
import rikmuld.camping.inventory.slot.SlotCrafting;
import rikmuld.camping.inventory.slot.SlotDisable;
import rikmuld.camping.inventory.slot.SlotItemsOnly;

public class ContainerCampinvTool extends ContainerMain {

	IInventory inv;	
    InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	InventoryCraftResult result = new InventoryCraftResult();
	
	EntityPlayer player;
	World worldObj;

	ArrayList<SlotDisable> slots = new ArrayList<SlotDisable>();
	int lanternDamage;
	
	public ContainerCampinvTool(EntityPlayer player)
	{		
		worldObj = player.worldObj;
		this.player = player;
		
		SlotDisable resultSlot = new SlotCrafting(player, craftMatrix, result, 0, 111, 35);
		slots.add(resultSlot);
		addSlot(resultSlot);

        for (int var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 3; ++var4)
            {
            	SlotDisable slot = new SlotDisable(craftMatrix, var4 + var3 * 3, 17 + var4 * 18, 17 + var3 * 18);
            	slots.add(slot);
                this.addSlotToContainer(slot);
            }
        }
		
		inv = new InventoryCampingInvTool(this, player, 3, slots);
		
		for(int row = 0; row<3; ++row)
		{
			for(int collom = 0; collom<1; ++collom)
			{
				addSlot(new SlotItemsOnly(inv, collom+row*1, 143+collom*18, 17+row*18, ModItems.knife.itemID, ModBlocks.lantern.blockID, Item.map.itemID));
			}
		}

		ContainerUtil.addSlots(this, player.inventory, 9, 3, 9, 8, 84);
		ContainerUtil.addSlots(this, player.inventory, 0, 1, 9, 8, 142);
		
		inv.openChest();
        this.onCraftMatrixChanged(this.craftMatrix);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{		
		inv.closeChest();
		
        if (!this.worldObj.isRemote)
        {
            for (int i = 0; i < 9; ++i)
            {
                ItemStack itemstack = this.craftMatrix.getStackInSlot(i);                
                if (itemstack != null)
                {
                    player.dropPlayerItem(itemstack);
                }              
            }
        }
        for(int i=0;i<9;++i)this.craftMatrix.setInventorySlotContents(i, null);
	}
	
	@Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
	{
        this.result.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
	}
	
    public ItemStack transferStackInSlot(EntityPlayer player, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(i<inv.getSizeInventory()+craftMatrix.getSizeInventory()+result.getSizeInventory())
			{
				if(!mergeItemStack(itemstack1, inv.getSizeInventory()+craftMatrix.getSizeInventory()+result.getSizeInventory(), inventorySlots.size(), true))
				{
					return null;
				}
			}
            else
            {
            	if(itemstack1.itemID==ModItems.knife.itemID||itemstack1.itemID==ModBlocks.lantern.blockID||itemstack1.itemID==Item.map.itemID)
            	{
            		if(!mergeItemStack(itemstack1, 10, 13, false))
    				{
    					return null;
    				}
            	}
            	else return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
    
    
    @Override
	public void addCraftingToCrafters(ICrafting crafter)
	{
		super.addCraftingToCrafters(crafter);
		crafter.sendProgressBarUpdate(this, 0, CampingInvUtil.getLanternDamage(player));
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0; i<this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			if(this.lanternDamage!=CampingInvUtil.getLanternDamage(player))
			{
				icrafting.sendProgressBarUpdate(this, 0, CampingInvUtil.getLanternDamage(player));
				((InventoryCampingInvTool)inv).readFromNBT(player.getEntityData().getCompoundTag("campInv").getCompoundTag("tool"));
			}
		}
		this.lanternDamage = CampingInvUtil.getLanternDamage(player);
	}
}
