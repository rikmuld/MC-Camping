package com.rikmuld.camping.inventory.objs

import net.minecraft.util.ResourceLocation
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11
import net.minecraft.client.gui.inventory.GuiContainer
import com.rikmuld.corerm.client.GuiContainerSimple
import com.rikmuld.camping.objs.Objs
import com.rikmuld.corerm.inventory.RMContainerItem
import com.rikmuld.corerm.inventory.SlotItemsNot
import com.rikmuld.corerm.inventory.SlotNoPickup
import net.minecraft.item.Item
import net.minecraft.inventory.Slot
import com.rikmuld.camping.Lib._
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.corerm.inventory.RMInventoryItem
import com.rikmuld.camping.objs.ItemDefinitions
import net.minecraft.item.ItemStack
import net.minecraft.inventory.Container

class BagContainer(player: EntityPlayer) extends RMContainerItem(player) {
  this.addSlots(invPlayer, 9, 3, 9, 8, 84)
  for (row <- 0 until 9) {
    if (row == invPlayer.currentItem) addSlotToContainer(new SlotNoPickup(invPlayer, row, 8 + (row * 18), 142))
    else addSlotToContainer(new Slot(invPlayer, row, 8 + (row * 18), 142))
  }

  override def getItemInv = new RMInventoryItem(player.inventory.getCurrentItem, player, 27, 64, true)
  override def getItem: Item = Objs.backpack;
}

abstract class BagGui(container: Container) extends GuiContainerSimple(container) {
  override def getTexture: String = TextureInfo.GUI_BAG;
  override def getName: String = new ItemStack(Objs.backpack, 1, getMeta).getDisplayName
  override def hasName: Boolean = true;
  def getMeta:Integer 
}

class BackpackGui(player: EntityPlayer) extends BagGui(new BackpackContainer(player)) {
  def getMeta = ItemDefinitions.Backpack.BACKPACK 
  
  protected override def drawGuiContainerBackgroundLayer(partialTick: Float, mouseX: Int, mouseY: Int) {
    super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY)
    drawTexturedModalRect(guiLeft + 61, guiTop + 25, 0, 166, 54, 54)  
  }
}

class BackpackContainer(player: EntityPlayer) extends BagContainer(player) {
  for (row <- 0 until 3; collom <- 3 until 6) this.addSlot(new SlotItemsNot(inv, collom + (row * 9), 8 + (collom * 18), 26 + (row * 18), Objs.backpack))

  override def getItemDamage: Int = ItemDefinitions.Backpack.BACKPACK
}

class PouchGui(player: EntityPlayer) extends BagGui(new PouchContainer(player)) {
  def getMeta = ItemDefinitions.Backpack.POUCH 
  
  protected override def drawGuiContainerBackgroundLayer(partialTick: Float, mouseX: Int, mouseY: Int) {
    super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY)
    drawTexturedModalRect(guiLeft + 61, guiTop + 43, 0, 166, 54, 18)  
  }
}

class PouchContainer(player: EntityPlayer) extends BagContainer(player) {
  for (row <- 1 until 2; collom <- 3 until 6) this.addSlot(new SlotItemsNot(inv, collom + (row * 9), 8 + (collom * 18), 26 + (row * 18), Objs.backpack))

  override def getItemDamage: Int = ItemDefinitions.Backpack.POUCH
}

class RucksackGui(player: EntityPlayer) extends BagGui(new RucksackContainer(player)) {
  def getMeta = ItemDefinitions.Backpack.RUCKSACK
  
  protected override def drawGuiContainerBackgroundLayer(partialTick: Float, mouseX: Int, mouseY: Int) {
    super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY)
    drawTexturedModalRect(guiLeft + 7, guiTop + 25, 0, 166, 162, 54)  
  }
}

class RucksackContainer(player: EntityPlayer) extends BagContainer(player) {
  for (row <- 0 until 3; collom <- 0 until 9) this.addSlot(new SlotItemsNot(inv, collom + (row * 9), 8 + (collom * 18), 26 + (row * 18), Objs.backpack))

  override def getItemDamage: Int = ItemDefinitions.Backpack.RUCKSACK
}