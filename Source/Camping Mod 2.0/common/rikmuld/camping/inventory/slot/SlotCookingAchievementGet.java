package rikmuld.camping.inventory.slot;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import rikmuld.camping.core.register.ModAchievements;
import rikmuld.camping.item.ItemKit;

public class SlotCookingAchievementGet extends SlotDisable {

	ArrayList<Integer> ID = new ArrayList<Integer>();
	EntityPlayer player;

	public SlotCookingAchievementGet(IInventory inventory, int slotIndex, int xPos, int yPos, EntityPlayer player, int... IDs)
	{
		super(inventory, slotIndex, xPos, yPos);
		for(Integer id: IDs)
		{
			ID.add(id);
		}
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		return ID.contains(-1)? true:ID.contains(is.itemID);
	}

	@Override
	public void putStack(ItemStack stack)
	{
		super.putStack(stack);

		if(stack != null)
		{
			if(stack.getItemDamage() == ItemKit.KIT_SPIT)
			{
				ModAchievements.spit.addStatToPlayer(player);
			}
			else if(stack.getItemDamage() == ItemKit.KIT_GRILL)
			{
				ModAchievements.grill.addStatToPlayer(player);
			}
			else if(stack.getItemDamage() == ItemKit.KIT_PAN)
			{
				ModAchievements.pan.addStatToPlayer(player);
			}
		}
	}
}
