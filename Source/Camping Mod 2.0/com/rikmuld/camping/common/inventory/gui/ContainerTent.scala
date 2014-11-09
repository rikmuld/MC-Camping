package com.rikmuld.camping.common.inventory.gui

import com.rikmuld.camping.common.inventory.SlotItemsOnly
import com.rikmuld.camping.common.objs.tile.TileEntityTent
import com.rikmuld.camping.core.Utils._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.init.Items
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import com.rikmuld.camping.common.inventory.SlotState

class ContainerTentLanterns(player: EntityPlayer, tile: IInventory) extends ContainerTileMain(player, tile) {
  private var tent: TileEntityTent = tile.asInstanceOf[TileEntityTent]

  addSlotToContainer(new SlotItemsOnly(tile, 0, 80, 88, Items.glowstone_dust))
  this.addSlots(player.inventory, 9, 3, 9, 8, 113)
  this.addSlots(player.inventory, 0, 1, 9, 8, 171)

  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(i).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (i < tent.getSizeInventory) {
        if (!mergeItemStack(itemstack1, tent.getSizeInventory, inventorySlots.size, true)) return null
      } else if (itemstack1.getItem() == Items.glowstone_dust) {
        if (!mergeItemStack(itemstack1, 0, tent.getSizeInventory, true)) return null
      }
      if (itemstack1.stackSize == 0) slot.putStack(null)
      else slot.onSlotChanged()
    }
    itemstack
  }
}

class ContainerTentChests(player: EntityPlayer, tile: IInventory) extends ContainerTileMain(player, tile) {
  private var tent: TileEntityTent = tile.asInstanceOf[TileEntityTent]
  var slots: Array[Array[SlotState]] = Array.ofDim[SlotState](25, 6)

  for (collom <- 0 until 25; row <- 0 until 6) {
    val slot = new SlotState(tile, row + (collom * 6) + 1, 9 + (collom * 18), 34 + (row * 18))
    addSlotToContainer(slot)
    slot.disable
    slots(collom)(row) = slot
  }
  this.addSlots(player.inventory, 9, 3, 9, 27, 146)
  this.addSlots(player.inventory, 0, 1, 9, 27, 204)
  tent.setSlots(slots)
  tent.manageSlots()

  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(i).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (i < tent.getSizeInventory) {
        if (!mergeItemStack(itemstack1, tent.getSizeInventory - 1, inventorySlots.size, true)) return null
      } else {
        if (!mergeItemStack(itemstack1, 0, tent.chests * 5 * 6, false)) return null
      }
      if (itemstack1.stackSize == 0) slot.putStack(null)
      else slot.onSlotChanged()
    }
    itemstack
  }
}