package rikmuld.camping.inventory.slot;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import rikmuld.camping.misc.achievements.AchievementMain;

public class SlotAchivementGet extends SlotDisable {

	ArrayList<Integer> ID = new ArrayList<Integer>();
	AchievementMain achievement;
	EntityPlayer player;
	
	public SlotAchivementGet(IInventory inventory, int slotIndex, int xPos, int yPos, EntityPlayer player, AchievementMain achievement, int... IDs)
	{
		super(inventory, slotIndex, xPos, yPos);
		for(Integer id:IDs)this.ID.add(id);
		this.achievement = achievement;
		this.player = player;
	}
	
    public void putStack(ItemStack stack)
    {
    	super.putStack(stack);
    	achievement.addStatToPlayer(player);
    }

	public boolean isItemValid(ItemStack is)
	{
		return ID.contains(-1)? true:ID.contains(is.itemID);
	}
}
