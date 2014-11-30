package com.rikmuld.camping.common.inventory

import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import com.rikmuld.camping.misc.CookingEquipment
import net.minecraft.inventory.Slot

class SlotCooking(par1iInventory: IInventory, id: Int, x: Int, y: Int) extends Slot(par1iInventory, id, x, y) {
  var active: Boolean = _
  var equipment: CookingEquipment = _
  var fire: TileEntityCampfireCook = _

  deActivate()

  def activate(x: Int, y: Int, equipment: CookingEquipment, fire: TileEntityCampfireCook) {
    active = true
    this.equipment = equipment
    this.fire = fire
    xDisplayPosition = x
    yDisplayPosition = y
  }
  def deActivate() {
    active = false
    equipment = null
    fire = null
    xDisplayPosition = -1000
    yDisplayPosition = -1000
  }
  override def getSlotStackLimit(): Int = 1
  override def isItemValid(stack: ItemStack): Boolean = if ((equipment != null) && (fire != null)) equipment.canCook(stack) else false
}