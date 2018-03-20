package com.rikmuld.camping.common.inventory

import com.rikmuld.corerm.gui.slots.SlotOnly
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.{Item, ItemStack}

class SlotItemMeta(inv: IInventory, index: Int, x: Int, y: Int, item: Item, damage: Vector[Int]) extends Slot(inv, index, x, y) with SlotOnly {
  override def getAllowedStacks: Vector[ItemStack] =
    damage.map(meta => new ItemStack(item, 1, meta))
}