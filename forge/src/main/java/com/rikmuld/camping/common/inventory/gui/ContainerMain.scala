package com.rikmuld.camping.common.inventory.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraft.init.Items
import net.minecraft.inventory.IInventory
import com.rikmuld.camping.common.inventory.inventory.InventoryItemMain
import com.rikmuld.camping.core.Utils._

abstract class ContainerItemMain(player: EntityPlayer) extends Container {
  val invPlayer: InventoryPlayer = player.inventory;
  val inv = getItemInv

  inv.openInventory()

  override def canInteractWith(player: EntityPlayer): Boolean = !player.isDead && inv.isUseableByPlayer(player)
  override def onContainerClosed(player: EntityPlayer) {
    super.onContainerClosed(player)
    inv.closeInventory()
    if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem().equals(getItem)) ((inv).setNBT(player.getCurrentEquippedItem()))
  }
  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = null;
    var slot = inventorySlots.get(i).asInstanceOf[Slot];
    if ((slot != null) && slot.getHasStack()) {
      var itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (i < inv.getSizeInventory()) {
        if (!mergeItemStack(itemstack1, inv.getSizeInventory(), inventorySlots.size(), true)) return null;
      } else if (!mergeItemStack(itemstack1, 0, inv.getSizeInventory(), false)) return null;
      if (itemstack1.stackSize == 0) slot.putStack(null);
      else slot.onSlotChanged();
    }
    itemstack;
  }
  def getItemInv: InventoryItemMain
  def getItem: Item
}

abstract class ContainerTileMain(player: EntityPlayer, tile: IInventory) extends Container {
  override def canInteractWith(player: EntityPlayer): Boolean = !player.isDead && tile.isUseableByPlayer(player)
  override def onContainerClosed(player: EntityPlayer) {
    super.onContainerClosed(player)
    tile.closeInventory()
  }
  override def transferStackInSlot(player: EntityPlayer, slotNum: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(slotNum).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (slotNum < tile.getSizeInventory) {
        if (!mergeItemStack(itemstack1, tile.getSizeInventory, inventorySlots.size, true)) return null
      } else if (!mergeItemStack(itemstack1, 0, tile.getSizeInventory(), false)) return null
      if (itemstack1.stackSize == 0) {
        slot.putStack(null)
      } else {
        slot.onSlotChanged()
      }
    }
    itemstack
  }
}