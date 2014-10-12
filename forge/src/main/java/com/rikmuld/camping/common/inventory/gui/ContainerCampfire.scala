package com.rikmuld.camping.common.inventory.gui

import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.init.Items
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.common.inventory.SlotItemsOnly

class ContainerCampfire(player: EntityPlayer, tile: IInventory) extends ContainerTileMain(player, tile) {
  override def addContainer {
    addSlotToContainer(new SlotItemsOnly(tile, 0, 71, 12, Items.dye))
    this.addSlots(player.inventory, 0, 1, 9, 8, 96)
    this.addSlots(player.inventory, 9, 3, 9, 8, 38)
  }
  override def transferStackInSlot(player: EntityPlayer, slotNum: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(slotNum).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (slotNum < tile.getSizeInventory) {
        if (!mergeItemStack(itemstack1, tile.getSizeInventory, inventorySlots.size, true)) return null
      } else {
        if (itemstack.getItem == Items.dye) {
          if (!mergeItemStack(itemstack1, 0, 1, false)) return null
        } else return null
      }
      if (itemstack1.stackSize == 0) {
        slot.putStack(null)
      } else {
        slot.onSlotChanged()
      }
    }
    itemstack
  }
}