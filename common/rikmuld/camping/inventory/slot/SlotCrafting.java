package rikmuld.camping.inventory.slot;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotCrafting extends SlotItemsOnly {

    private final IInventory craftMatrix;
    private EntityPlayer thePlayer;
    private int amountCrafted;
    
	public SlotCrafting(EntityPlayer player, IInventory matrix, IInventory par1iInventory, int id, int x, int y)
	{
		super(par1iInventory, id, x, y);
	    this.thePlayer = player;
	    this.craftMatrix = matrix;
	}

    public ItemStack decrStackSize(int par1)
    {
        if (this.getHasStack())
        {
            this.amountCrafted += Math.min(par1, this.getStack().stackSize);
        }

        return super.decrStackSize(par1);
    }

    protected void onCrafting(ItemStack par1ItemStack, int par2)
    {
        this.amountCrafted += par2;
        this.onCrafting(par1ItemStack);
    }

    protected void onCrafting(ItemStack par1ItemStack)
    {
        par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
        this.amountCrafted = 0;

        if (par1ItemStack.itemID == Block.workbench.blockID)
        {
            this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
        }
        else if (par1ItemStack.itemID == Item.pickaxeWood.itemID)
        {
            this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
        }
        else if (par1ItemStack.itemID == Block.furnaceIdle.blockID)
        {
            this.thePlayer.addStat(AchievementList.buildFurnace, 1);
        }
        else if (par1ItemStack.itemID == Item.hoeWood.itemID)
        {
            this.thePlayer.addStat(AchievementList.buildHoe, 1);
        }
        else if (par1ItemStack.itemID == Item.bread.itemID)
        {
            this.thePlayer.addStat(AchievementList.makeBread, 1);
        }
        else if (par1ItemStack.itemID == Item.cake.itemID)
        {
            this.thePlayer.addStat(AchievementList.bakeCake, 1);
        }
        else if (par1ItemStack.itemID == Item.pickaxeStone.itemID)
        {
            this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
        }
        else if (par1ItemStack.itemID == Item.swordWood.itemID)
        {
            this.thePlayer.addStat(AchievementList.buildSword, 1);
        }
        else if (par1ItemStack.itemID == Block.enchantmentTable.blockID)
        {
            this.thePlayer.addStat(AchievementList.enchantments, 1);
        }
        else if (par1ItemStack.itemID == Block.bookShelf.blockID)
        {
            this.thePlayer.addStat(AchievementList.bookcase, 1);
        }
    }

    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        GameRegistry.onItemCrafted(par1EntityPlayer, par2ItemStack, craftMatrix);
        this.onCrafting(par2ItemStack);

        for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);

            if (itemstack1 != null)
            {
                this.craftMatrix.decrStackSize(i, 1);

                if (itemstack1.getItem().hasContainerItem())
                {
                    ItemStack itemstack2 = itemstack1.getItem().getContainerItemStack(itemstack1);

                    if (itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage())
                    {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
                        itemstack2 = null;
                    }

                    if (itemstack2 != null && (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1) || !this.thePlayer.inventory.addItemStackToInventory(itemstack2)))
                    {
                        if (this.craftMatrix.getStackInSlot(i) == null)
                        {
                            this.craftMatrix.setInventorySlotContents(i, itemstack2);
                        }
                        else
                        {
                            this.thePlayer.dropPlayerItem(itemstack2);
                        }
                    }
                }
            }
        }
    }
}
