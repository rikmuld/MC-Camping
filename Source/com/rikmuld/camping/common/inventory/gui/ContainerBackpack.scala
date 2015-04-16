package com.rikmuld.camping.common.inventory.gui

import com.rikmuld.camping.core.Objs
import com.rikmuld.corerm.core.CoreUtils.ContainerUtils
import com.rikmuld.corerm.common.inventory.inventory.InventoryItemMain
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import com.rikmuld.corerm.common.inventory.gui.ContainerItemMain
import com.rikmuld.corerm.common.inventory.SlotItemsNot
import com.rikmuld.corerm.common.inventory.SlotNoPickup

class ContainerBackpack(player: EntityPlayer) extends ContainerItemMain(player) {
  for (row <- 0 until 3; collom <- 0 until 9) this.addSlot(new SlotItemsNot(inv, collom + (row * 9), 8 + (collom * 18), 26 + (row * 18), Objs.backpack))
  this.addSlots(invPlayer, 9, 3, 9, 8, 84)
  for (row <- 0 until 9) {
    if (row == invPlayer.currentItem) addSlotToContainer(new SlotNoPickup(invPlayer, row, 8 + (row * 18), 142))
    else addSlotToContainer(new Slot(invPlayer, row, 8 + (row * 18), 142))
  }

  override def getItemInv = new InventoryItemMain(player.getCurrentEquippedItem, player, 27, 64, true);
  override def getItem: Item = Objs.backpack
}