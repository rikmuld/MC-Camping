package com.rikmuld.camping.common.inventory

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import java.util.ArrayList
import net.minecraft.item.Item
import scala.collection.mutable.ListBuffer
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook
import com.rikmuld.camping.misc.CookingEquipment

class SlotDisable(inv: IInventory, id: Int, var xFlag: Int, var yFlag: Int) extends Slot(inv, id, xFlag, yFlag) {
  def disable = {
    xDisplayPosition = -500
    yDisplayPosition = -500
  }
  def enable = {
    xDisplayPosition = xFlag;
    yDisplayPosition = yFlag;
  }
}

class SlotItemsNot(inventory: IInventory, slotIndex: Int, xPos: Int, yPos: Int, stacks: AnyRef*) extends SlotDisable(inventory, slotIndex, xPos, yPos) {
  var alowedStacks: ListBuffer[Any] = new ListBuffer[Any]()

  for (stack <- stacks) alowedStacks.append(stack)

  def this(tile: IInventory, slotIndex: Int, xPos: Int, yPos: Int, set: Set[Any]) {
    this(tile, slotIndex, xPos, yPos)
    for (id <- set) alowedStacks.append(id)
  }

  override def isItemValid(is: ItemStack): Boolean = {
    var flag = false
    for (stack <- alowedStacks) if (stack.isInstanceOf[ItemStack] && !stack.asInstanceOf[ItemStack].isItemEqual(is)) flag = true else if (stack.isInstanceOf[Item] && is.getItem != stack) flag = true
    if (alowedStacks.size == 0) true else flag == true
  }
}

class SlotNoPickup(inventory: IInventory, slotIndex: Int, xPos: Int, yPos: Int) extends Slot(inventory, slotIndex, xPos, yPos) {
  override def canTakeStack(par1EntityPlayer: EntityPlayer): Boolean = false
}

class SlotItemsOnly(inventory: IInventory, slotIndex: Int, xPos: Int, yPos: Int, stacks: AnyRef*) extends SlotDisable(inventory, slotIndex, xPos, yPos) {
  var alowedStacks: ListBuffer[Any] = new ListBuffer[Any]()
  for (stack <- stacks) alowedStacks.append(stack)

  def this(tile: IInventory, slotIndex: Int, xPos: Int, yPos: Int, set: Set[Any]) {
    this(tile, slotIndex, xPos, yPos)
    for (id <- set) alowedStacks.append(id)
  }

  override def isItemValid(is: ItemStack): Boolean = {
    var flag = false
    for (stack <- alowedStacks) {
      if (stack.isInstanceOf[ItemStack] && stack.asInstanceOf[ItemStack].isItemEqual(is)) flag = true
      else if (stack.isInstanceOf[Item] && is.getItem.equals(stack)) flag = true
    }
    if (alowedStacks.size == 0) true else flag == true
  }
}

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

class SlotState(inv: IInventory, id: Int, x: Int, y: Int) extends SlotDisable(inv, id, x, y) {
  var stateX: Int = _
  var stateY: Int = _

  override def enable() {
    xDisplayPosition = stateX
    yDisplayPosition = stateY
  }
  def setStateX(state: Int) = stateX = xFlag - (18 * state)
  def setStateY(state: Int) = stateY = yFlag - (18 * state)
}