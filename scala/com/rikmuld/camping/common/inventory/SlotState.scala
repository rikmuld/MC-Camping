package com.rikmuld.camping.common.inventory

import com.rikmuld.corerm.gui.slots.SlotDisable
import net.minecraft.inventory.{IInventory, Slot}

class SlotState(inv: IInventory, id: Int, x: Int, y: Int) extends Slot(inv, id, x, y) with SlotDisable {
  var stateX: Int = xFlag
  var stateY: Int = yFlag

  override def enable() {
    xPos = stateX
    yPos = stateY
  }

  def setStateX(state: Int) =
    stateX = xFlag - (18 * state)

  def setStateY(state: Int) =
    stateY = yFlag - (18 * state)
}