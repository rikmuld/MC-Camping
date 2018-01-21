package com.rikmuld.camping.inventory

import scala.collection.mutable.ListBuffer
import com.rikmuld.camping.misc.CookingEquipment
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.entity.item.EntityPainting
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.SlotCrafting
import com.rikmuld.corerm.inventory.SlotWithItemsNot
import com.rikmuld.corerm.inventory.SlotWithDisable
import net.minecraft.inventory.InventoryCrafting
import com.rikmuld.camping.objs.tile.TileCampfireCook

class SlotState(inv: IInventory, id: Int, x: Int, y: Int) extends Slot(inv, id, x, y) with SlotWithDisable{
  setDisableSlot(x, y)
  
  var stateX: Int = xFlag
  var stateY: Int = yFlag

  override def enable() {
    xPos = stateX
    yPos = stateY
  }
  def setStateX(state: Int) = stateX = xFlag - (18 * state)
  def setStateY(state: Int) = stateY = yFlag - (18 * state)
}

class SlotCooking(inv: IInventory, id: Int, x: Int, y: Int) extends Slot(inv, id, x, y) {
  var active: Boolean = _
  var equipment: CookingEquipment = _
  var fire: TileCampfireCook = _

  deActivate()

  def activate(x: Int, y: Int, equipment: CookingEquipment, fire: TileCampfireCook) {
    active = true
    this.equipment = equipment
    this.fire = fire
    xPos = x
    yPos = y
  }
  def deActivate() {
    active = false
    equipment = null
    fire = null
    xPos = -1000
    yPos = -1000
  }
  override def getSlotStackLimit(): Int = 1
  override def isItemValid(stack: ItemStack): Boolean = if ((equipment != null) && (fire != null)) equipment.canCook(stack) else false
}
