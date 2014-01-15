package rikmuld.camping.inventory.slot;

import net.minecraft.inventory.IInventory;

public class SlotState extends SlotDisable{

	int stateX;
	int stateY;
	
	public SlotState(IInventory inv, int id, int x, int y) 
	{
		super(inv, id, x, y);
		this.stateX = this.xFlag;
		this.stateY = this.yFlag;
	}
	
	public void setStateX(int state)
	{
		this.stateX = this.xFlag-18*state;
	}
	
	public void setStateY(int state)
	{
		this.stateY = this.yFlag-18*state;
	}
	
	public void enable()
	{
		this.xDisplayPosition = stateX;
		this.yDisplayPosition = stateY;
	}
}
