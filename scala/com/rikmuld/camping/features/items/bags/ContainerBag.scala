package com.rikmuld.camping.features.items.bags

import com.rikmuld.camping.inventory.SlotBackpack
import com.rikmuld.corerm.gui.container.ContainerItem
import com.rikmuld.corerm.inventory.InventoryItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

class ContainerBag(player: EntityPlayer, activeSlots: Seq[Int]) extends ContainerItem(player) {
  override def initIInventory =
    new InventoryItem(player.inventory.getCurrentItem, 27, 64)

  def addInventorySlots(): Unit =
    for (row <- 0 until 3; column <- 0 until 9) {
      val id = column + (row * 9)

      addSlotToContainer(
        new SlotBackpack(getIInventory, id, 8 + (column * 18), 26 + (row * 18), activeSlots.contains(id))
      )
    }

  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean =
    activeSlots.exists(i => mergeItemStack(stack, i, i + 1, false))
}

class ContainerBackpack(player: EntityPlayer) extends ContainerBag(player, (3 until 6) ++ (12 until 15) ++ (21 until 24))

class ContainerPouch(player: EntityPlayer) extends ContainerBag(player, 12 until 15)

class ContainerRucksack(player: EntityPlayer) extends ContainerBag(player, 0 until 27)