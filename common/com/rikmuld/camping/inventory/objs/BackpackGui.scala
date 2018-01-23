package com.rikmuld.camping.inventory.objs

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.inventory.SlotBackpack
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.ItemDefinitions.Backpack._
import com.rikmuld.corerm.inventory.container.ContainerItem
import com.rikmuld.corerm.inventory.gui.GuiContainerSimple
import com.rikmuld.corerm.inventory.inventory.InventoryItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

abstract class BagContainer(player: EntityPlayer) extends ContainerItem(player) {
  override def initIInventory =
    new InventoryItem(player.inventory.getCurrentItem, 27, 64)

  def addInventorySlots(): Unit =
    for (row <- 0 until 3; column <- 0 until 9) {
      val id = column + (row * 9)

      addSlotToContainer(
        new SlotBackpack(getIInventory, id, 8 + (column * 18), 26 + (row * 18), activeSlots.contains(id))
      )
    }

  def activeSlots: Seq[Int]
}

abstract class BagGui(container: Container, metaData: Int) extends GuiContainerSimple(container) {
  override def getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_BAG)

  override def getName: String =
    new ItemStack(Objs.backpack, 1, metaData).getDisplayName

  override def hasName: Boolean =
    true

  override def drawGUI(mouseX: Int, mouseY: Int) {
    super.drawGUI(mouseX, mouseY)
    drawSlots()
  }

  def drawSlots(): Unit
}

class BackpackContainer(player: EntityPlayer) extends BagContainer(player) {
  def activeSlots =
    (3 until 6) ++ (12 until 15) ++ (21 until 24)

  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean =
    mergeItemStack(stack, 3, 6, false) ||
      mergeItemStack(stack, 12, 15, false) ||
      mergeItemStack(stack, 21, 24, false)
}

class PouchContainer(player: EntityPlayer) extends BagContainer(player) {
  def activeSlots =
    12 until 15

  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean =
    mergeItemStack(stack, 12, 15, false)
}

class RucksackContainer(player: EntityPlayer) extends BagContainer(player) {
  def activeSlots =
    0 until 27
}

class PouchGui(player: EntityPlayer) extends BagGui(new PouchContainer(player), POUCH) {
  def drawSlots() =
    drawTexturedModalRect(guiLeft + 61, guiTop + 43, 0, 166, 54, 18)
}

class BackpackGui(player: EntityPlayer) extends BagGui(new BackpackContainer(player), BACKPACK) {
  def drawSlots() =
    drawTexturedModalRect(guiLeft + 61, guiTop + 25, 0, 166, 54, 54)
}

class RucksackGui(player: EntityPlayer) extends BagGui(new RucksackContainer(player), RUCKSACK) {
  def drawSlots() =
    drawTexturedModalRect(guiLeft + 7, guiTop + 25, 0, 166, 162, 54)
}