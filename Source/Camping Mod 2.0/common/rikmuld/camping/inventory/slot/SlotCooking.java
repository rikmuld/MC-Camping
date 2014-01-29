package rikmuld.camping.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import rikmuld.camping.entity.tileentity.TileEntityCampfireCook;
import rikmuld.camping.misc.cooking.CookingEquipment;

public class SlotCooking extends Slot {

	public boolean active;
	public CookingEquipment equipment;
	public TileEntityCampfireCook fire;

	public SlotCooking(IInventory par1iInventory, int id, int x, int y)
	{
		super(par1iInventory, id, x, y);
		deActivate();
	}

	public void activate(int x, int y, CookingEquipment equipment, TileEntityCampfireCook fire)
	{
		active = true;
		this.equipment = equipment;
		this.fire = fire;

		xDisplayPosition = x;
		yDisplayPosition = y;
	}

	public void deActivate()
	{
		active = false;
		equipment = null;
		fire = null;

		xDisplayPosition = -1000;
		yDisplayPosition = -1000;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return (equipment != null) && (fire != null)? equipment.canCook(stack.itemID, stack.getItemDamage()) && (fire.getStackInSlot(12) != null)? true:(equipment.getCookedFood(stack.itemID, stack.getItemDamage()) != null):false;
	}
}
