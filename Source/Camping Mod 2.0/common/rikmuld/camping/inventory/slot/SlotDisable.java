package rikmuld.camping.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotDisable extends Slot {

	int xFlag, yFlag;

	public SlotDisable(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
		xFlag = par3;
		yFlag = par4;
	}

	public void disable()
	{
		xDisplayPosition = yDisplayPosition = -500;
	}

	public void enable()
	{
		xDisplayPosition = xFlag;
		yDisplayPosition = yFlag;
	}
}
