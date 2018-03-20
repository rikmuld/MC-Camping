package com.rikmuld.camping.inventory

import net.minecraft.inventory.{IInventory, Slot}

class SlotTabbed(inv: IInventory, index: Int, x:Int, y:Int, val tab: Int) extends
  Slot(inv, index, x, y) with SlotTabbed