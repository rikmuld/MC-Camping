package rikmuld.camping.inventory.slot;

import net.minecraft.inventory.IInventory;

public class SlotState extends SlotDisable {

	int stateX;
	int stateY;

	public SlotState(IInventory inv, int id, int x, int y)
	{
		super(inv, id, x, y);
		stateX = xFlag;
		stateY = yFlag;
	}

	@Override
	public void enable()
	{
		xDisplayPosition = stateX;
		yDisplayPosition = stateY;
	}

	public void setStateX(int state)
	{
		stateX = xFlag - (18 * state);
	}

	public void setStateY(int state)
	{
		stateY = yFlag - (18 * state);
	}
}
