package com.rikmuld.camping.inventory

import com.rikmuld.corerm.gui.slots.SlotOnly
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.Item

class SlotItem(inv: IInventory, index: Int, x: Int, y: Int, item: Item) extends Slot(inv, index, x, y) with SlotOnly {
  override def getAllowedItems: Vector[Item] =
    Vector(item)
}