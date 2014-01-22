package rikmuld.camping.inventory.slot;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class SlotCrafting extends SlotItemsOnly {

	private final IInventory craftMatrix;
	private EntityPlayer thePlayer;
	private int amountCrafted;

	public SlotCrafting(EntityPlayer player, IInventory matrix, IInventory par1iInventory, int id, int x, int y)
	{
		super(par1iInventory, id, x, y);
		thePlayer = player;
		craftMatrix = matrix;
	}

	@Override
	public ItemStack decrStackSize(int par1)
	{
		if(getHasStack())
		{
			amountCrafted += Math.min(par1, getStack().stackSize);
		}

		return super.decrStackSize(par1);
	}

	@Override
	protected void onCrafting(ItemStack par1ItemStack)
	{
		par1ItemStack.onCrafting(thePlayer.worldObj, thePlayer, amountCrafted);
		amountCrafted = 0;

		if(par1ItemStack.itemID == Block.workbench.blockID)
		{
			thePlayer.addStat(AchievementList.buildWorkBench, 1);
		}
		else if(par1ItemStack.itemID == Item.pickaxeWood.itemID)
		{
			thePlayer.addStat(AchievementList.buildPickaxe, 1);
		}
		else if(par1ItemStack.itemID == Block.furnaceIdle.blockID)
		{
			thePlayer.addStat(AchievementList.buildFurnace, 1);
		}
		else if(par1ItemStack.itemID == Item.hoeWood.itemID)
		{
			thePlayer.addStat(AchievementList.buildHoe, 1);
		}
		else if(par1ItemStack.itemID == Item.bread.itemID)
		{
			thePlayer.addStat(AchievementList.makeBread, 1);
		}
		else if(par1ItemStack.itemID == Item.cake.itemID)
		{
			thePlayer.addStat(AchievementList.bakeCake, 1);
		}
		else if(par1ItemStack.itemID == Item.pickaxeStone.itemID)
		{
			thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		}
		else if(par1ItemStack.itemID == Item.swordWood.itemID)
		{
			thePlayer.addStat(AchievementList.buildSword, 1);
		}
		else if(par1ItemStack.itemID == Block.enchantmentTable.blockID)
		{
			thePlayer.addStat(AchievementList.enchantments, 1);
		}
		else if(par1ItemStack.itemID == Block.bookShelf.blockID)
		{
			thePlayer.addStat(AchievementList.bookcase, 1);
		}
	}

	@Override
	protected void onCrafting(ItemStack par1ItemStack, int par2)
	{
		amountCrafted += par2;
		this.onCrafting(par1ItemStack);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
	{
		GameRegistry.onItemCrafted(par1EntityPlayer, par2ItemStack, craftMatrix);
		this.onCrafting(par2ItemStack);

		for(int i = 0; i < craftMatrix.getSizeInventory(); ++i)
		{
			ItemStack itemstack1 = craftMatrix.getStackInSlot(i);

			if(itemstack1 != null)
			{
				craftMatrix.decrStackSize(i, 1);

				if(itemstack1.getItem().hasContainerItem())
				{
					ItemStack itemstack2 = itemstack1.getItem().getContainerItemStack(itemstack1);

					if(itemstack2.isItemStackDamageable() && (itemstack2.getItemDamage() > itemstack2.getMaxDamage()))
					{
						MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
						itemstack2 = null;
					}

					if((itemstack2 != null) && (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1) || !thePlayer.inventory.addItemStackToInventory(itemstack2)))
					{
						if(craftMatrix.getStackInSlot(i) == null)
						{
							craftMatrix.setInventorySlotContents(i, itemstack2);
						}
						else
						{
							thePlayer.dropPlayerItem(itemstack2);
						}
					}
				}
			}
		}
	}
}
