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

class BagContainer(player: EntityPlayer, activeSlots: Seq[Int]) extends ContainerItem(player) {
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

abstract class BagGui(container: Container, metaData: Int, x: Int, y: Int, width: Int, height: Int) extends GuiContainerSimple(container) {
  val getTexture: ResourceLocation =
    new ResourceLocation(TextureInfo.GUI_BAG)

  override def getName: String =
    new ItemStack(Objs.backpack, 1, metaData).getDisplayName

  override def hasName: Boolean =
    true

  override def drawGUI(mouseX: Int, mouseY: Int) {
    super.drawGUI(mouseX, mouseY)
    drawTexturedModalRect(guiLeft + x, guiTop + y, 0, 166, width, height)
  }
}

class BackpackContainer(player: EntityPlayer) extends BagContainer(player, (3 until 6) ++ (12 until 15) ++ (21 until 24))

class PouchContainer(player: EntityPlayer) extends BagContainer(player, 12 until 15)

class RucksackContainer(player: EntityPlayer) extends BagContainer(player, 0 until 27)

class PouchGui(player: EntityPlayer) extends BagGui(new PouchContainer(player), POUCH, 61, 43, 54, 18)

class BackpackGui(player: EntityPlayer) extends BagGui(new BackpackContainer(player), BACKPACK, 61, 25, 54, 54)

class RucksackGui(player: EntityPlayer) extends BagGui(new RucksackContainer(player), RUCKSACK, 7, 25, 162, 54)