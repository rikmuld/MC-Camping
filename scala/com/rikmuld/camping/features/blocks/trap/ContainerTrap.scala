package com.rikmuld.camping.features.blocks.trap

import com.rikmuld.corerm.gui.container.ContainerSimple
import com.rikmuld.corerm.gui.slots.SlotOnlyItems
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, Slot}

class ContainerTrap(player: EntityPlayer, inv: IInventory) extends ContainerSimple[IInventory](player) {
  def addInventorySlots(): Unit =
    addSlotToContainer(new Slot(inv, 0, 80, 12) with SlotOnlyItems)

  override def playerInvY: Int =
    38

  override def initIInventory: IInventory =
    inv

  def getID: String =
    inv.getName
}