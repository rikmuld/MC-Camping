package rikmuld.camping.inventory.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public abstract class ContainerMain extends Container {

	public void addSlot(Slot slot)
	{
		addSlotToContainer(slot);
	}
}
