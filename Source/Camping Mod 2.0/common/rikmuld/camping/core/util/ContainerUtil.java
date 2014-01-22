package rikmuld.camping.core.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import rikmuld.camping.inventory.container.ContainerMain;

public class ContainerUtil {

	public static void addSlots(ContainerMain container, IInventory inv, int slotID, int rowMax, int collomMax, int xStart, int yStart)
	{
		for(int row = 0; row < rowMax; ++row)
		{
			for(int collom = 0; collom < collomMax; ++collom)
			{
				container.addSlot(new Slot(inv, collom + (row * collomMax) + slotID, xStart + (collom * 18), yStart + (row * 18)));
			}
		}
	}
}
