package com.rikmuld.camping.inventory.objs

import com.rikmuld.camping.Lib.TextureInfo
import com.rikmuld.corerm.gui.container.ContainerSimple
import com.rikmuld.corerm.gui.gui.GuiContainerSimple
import com.rikmuld.corerm.gui.slots.SlotOnlyItems
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, Slot}
import net.minecraft.util.ResourceLocation

class GuiTrap(player: EntityPlayer, inv: IInventory) extends GuiContainerSimple(new ContainerTrap(player, inv)) {
  ySize = 120

  val getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_TRAP)
}

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