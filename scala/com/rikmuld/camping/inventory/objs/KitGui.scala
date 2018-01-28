package com.rikmuld.camping.inventory.objs

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.misc.CookingEquipment._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.corerm.gui.container.ContainerItem
import com.rikmuld.corerm.gui.gui.GuiContainerSimple
import com.rikmuld.corerm.inventory.InventoryItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
  
class KitGui(player: EntityPlayer) extends GuiContainerSimple(new KitContainer(player)) {
  ySize = 181

  val getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_KIT)
}

class KitContainer(player: EntityPlayer) extends ContainerItem(player) {
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
    val equipment = getCookingForRecipe(getIInventory.getContents)

    player.inventory.getCurrentItem.setItemDamage(equipment.map(_.itemInfo.getItemDamage).getOrElse(
      if(!getIInventory.isEmpty) Kit.USELESS
      else Kit.EMPTY
    ))

    super.onContainerClosed(player)
  }
}