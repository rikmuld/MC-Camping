package com.rikmuld.camping.common.inventory

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import java.util.ArrayList
import net.minecraft.item.Item
import scala.collection.mutable.ListBuffer
import net.minecraft.entity.player.EntityPlayer

class SlotDisable(inv:IInventory, id:Int, var xFlag:Int, var yFlag:Int) extends Slot(inv, id, xFlag, yFlag) {
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
    for (stack <- alowedStacks) if (stack.isInstanceOf[ItemStack] && stack.asInstanceOf[ItemStack].isItemEqual(is)) flag = true else if (stack.isInstanceOf[Item] && is.getItem == stack) flag = true
    if (alowedStacks.size == 0) true else flag == true
  }
}