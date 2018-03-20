package com.rikmuld.camping.common.inventory

import net.minecraft.inventory.{IInventory, Slot}

class SlotTabbed(inv: IInventory, index: Int, x:Int, y:Int, val tab: Int) extends
  Slot(inv, index, x, y) with com.rikmuld.corerm.gui.slots.SlotTabbed
