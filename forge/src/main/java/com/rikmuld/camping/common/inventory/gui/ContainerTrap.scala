package com.rikmuld.camping.common.inventory.gui

import com.rikmuld.camping.common.inventory.SlotNoPickup
import com.rikmuld.camping.common.inventory.inventory.InventoryItemMain
import com.rikmuld.camping.core.Utils._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import com.rikmuld.camping.common.inventory.SlotItem

class ContainerTrap(player: EntityPlayer, inv: IInventory) extends ContainerTileMain(player, inv) {
  addSlotToContainer(new SlotItem(inv, 0, 80, 12))

  this.addSlots(player.inventory, 9, 3, 9, 8, 38)
  this.addSlots(player.inventory, 0, 1, 9, 8, 96)

  override def transferStackInSlot(player: EntityPlayer, slotNum: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(slotNum).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy
      if (slotNum < inv.getSizeInventory) {
        if (!mergeItemStack(itemstack1, inv.getSizeInventory, inventorySlots.size, true)) return null
      } else if (Block.getBlockFromItem(itemstack1.getItem()) != Blocks.air || (!mergeItemStack(itemstack1, 0, inv.getSizeInventory, false))) return null
      if (itemstack1.stackSize == 0) {
        slot.putStack(null)
      } else {
        slot.onSlotChanged
      }
    }
    itemstack
  }
}