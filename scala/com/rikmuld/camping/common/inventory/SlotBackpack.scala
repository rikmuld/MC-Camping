package com.rikmuld.camping.common.inventory

import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.gui.slots.{SlotDisable, SlotNot}
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.Item

class SlotBackpack(inv: IInventory, index: Int, x:Int, y:Int, active: Boolean) extends Slot(inv, index, x, y) with SlotDisable with SlotNot {
  if(!active) disable()

  override def getBanItems: Vector[Item] =
    Vector(ObjRegistry.backpack)
}