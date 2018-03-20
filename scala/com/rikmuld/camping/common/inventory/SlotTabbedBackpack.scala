package com.rikmuld.camping.common.inventory

import com.rikmuld.corerm.gui.slots.SlotChangingInventory
import net.minecraft.inventory.IInventory

class SlotTabbedBackpack(index: Int, x:Int, y:Int, val tab: Int) extends
  SlotBackpack(null, index, x, y, false) with com.rikmuld.corerm.gui.slots.SlotTabbed with SlotChangingInventory {

  private var inv: Option[IInventory] =
    None

  override def getIInventory: Option[IInventory] =
    inv

  override def setIInventory(inventory: Option[IInventory]): Unit =
    inv = inventory
}