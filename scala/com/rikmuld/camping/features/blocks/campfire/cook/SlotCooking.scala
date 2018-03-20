package com.rikmuld.camping.features.blocks.campfire.cook

import com.rikmuld.camping.features.blocks.campfire.cook.equipment.CookingEquipment
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.item.ItemStack

class SlotCooking(inv: IInventory, id: Int, x: Int, y: Int) extends Slot(inv, id, x, y) {
  var active: Boolean =
    false

  var equipment: Option[CookingEquipment] =
    None

  var fire: Option[TileEntityCampfireCook] =
    None

  deActivate()

  def activate(x: Int, y: Int, equip: CookingEquipment, tile: TileEntityCampfireCook): Unit = {
    active = true

    equipment = Some(equip)
    fire = Some(tile)

    xPos = x
    yPos = y
  }

  def deActivate(): Unit = {
    active = false

    equipment = None
    fire = None

    xPos = -1000
    yPos = -1000
  }

  override def getSlotStackLimit: Int =
    1

  override def isItemValid(stack: ItemStack): Boolean =
    equipment.exists(_.canCook(stack))
}