package com.rikmuld.camping.features.items.kit

import com.rikmuld.camping.Definitions.Kit
import com.rikmuld.camping.features.blocks.campfire.cook.equipment.CookingEquipment
import com.rikmuld.corerm.gui.container.ContainerItem
import com.rikmuld.corerm.inventory.InventoryItem
import net.minecraft.entity.player.EntityPlayer

class ContainerKit(player: EntityPlayer) extends ContainerItem(player) {
  override def playerInvY: Int =
    99

  override def addInventorySlots(): Unit = {
    addSlots(getIInventory, 0, 1, 5, 44, 16)
    addSlots(getIInventory, 5, 2, 1, 44, 34)
    addSlots(getIInventory, 7, 2, 1, 116, 34)
    addSlots(getIInventory, 9, 1, 5, 44, 70)
  }

  override def initIInventory =
    new InventoryItem(player.inventory.getCurrentItem, 14, 1)

  override def onContainerClosed(player: EntityPlayer) {
    val equipment = CookingEquipment.getEquipment(getIInventory.getContents)

    player.inventory.getCurrentItem.setItemDamage(equipment.map(_.getKitDamage).getOrElse(
      if(!getIInventory.isEmpty) Kit.USELESS
      else Kit.EMPTY
    ))

    super.onContainerClosed(player)
  }
}