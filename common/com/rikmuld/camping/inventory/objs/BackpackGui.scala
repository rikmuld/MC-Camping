package com.rikmuld.camping.inventory.objs

import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.client.GuiContainerSimple
import com.rikmuld.camping.objs.Objs
import com.rikmuld.corerm.inventory._
import net.minecraft.item.Item
import net.minecraft.inventory.Slot
import com.rikmuld.camping.Lib._
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.camping.objs.ItemDefinitions
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.inventory.Container

abstract class BagContainer(player: EntityPlayer) extends RMContainerItem(player) {
  for (row <- 0 until 3; collom <- 0 until 9) {
    val slot = new SlotItemsNotDisable(inv, collom + (row * 9), 8 + (collom * 18), 26 + (row * 18), Objs.backpack)
    slot.disable
    this.addSlot(slot)
  }
  addPlayerSlots()

  def addPlayerSlots(){
    for (row <- 0 until 9) {
      if (row == invPlayer.currentItem) addSlotToContainer(new SlotNoPickup(invPlayer, row, 8 + (row * 18), 142))
      else addSlotToContainer(new Slot(invPlayer, row, 8 + (row * 18), 142))
    }
    this.addSlots(invPlayer, 9, 3, 9, 8, 84)
  }
  override def getItemInv = {
    new RMInventoryItem(player.inventory.getCurrentItem, player, 27, 64, true)
  }
  def getSize():Int
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
  for(i <- 3 until 6) getSlot(i).asInstanceOf[SlotWithDisable].enable
  for(i <- 12 until 15) getSlot(i).asInstanceOf[SlotWithDisable].enable
  for(i <- 21 until 24) getSlot(i).asInstanceOf[SlotWithDisable].enable
  addPlayerSlots()
  
  override def getItemDamage: Int = ItemDefinitions.Backpack.BACKPACK
  def getSize = 9

  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = ItemStack.EMPTY
    var slot = inventorySlots.get(i)
    if ((slot != null) && slot.getHasStack()) {
      var itemstack1 = slot.getStack()
      itemstack = itemstack1.copy()
      if (i < inv.getSizeInventory()) {
        if (!mergeItemStack(itemstack1, inv.getSizeInventory(), inventorySlots.size(), false)) return ItemStack.EMPTY
      } else if (!mergeItemStack(itemstack1, 3, 6, false) && !mergeItemStack(itemstack1, 12, 15, false) && !mergeItemStack(itemstack1, 21, 24, false)) {
        if(i < inv.getSizeInventory + 9){
          if(!mergeItemStack(itemstack1, inv.getSizeInventory + 9, inv.getSizeInventory + 9 + 27, false)) return ItemStack.EMPTY
        } else {
          if(!mergeItemStack(itemstack1, inv.getSizeInventory, inv.getSizeInventory + 9, false)) return ItemStack.EMPTY
        }
      }
      if (itemstack1.getCount == 0) slot.putStack(new ItemStack(Items.AIR, 0))
      else slot.onSlotChanged()
    }
    itemstack
  }
}

class PouchGui(player: EntityPlayer) extends BagGui(new PouchContainer(player)) {
  def getMeta = ItemDefinitions.Backpack.POUCH 
  
  protected override def drawGuiContainerBackgroundLayer(partialTick: Float, mouseX: Int, mouseY: Int) {
    super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY)
    drawTexturedModalRect(guiLeft + 61, guiTop + 43, 0, 166, 54, 18)  
  }
}

class PouchContainer(player: EntityPlayer) extends BagContainer(player) {
  for(i <- 12 until 15) getSlot(i).asInstanceOf[SlotWithDisable].enable

  override def getItemDamage: Int = ItemDefinitions.Backpack.POUCH
  def getSize = 3

  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = ItemStack.EMPTY
    var slot = inventorySlots.get(i)
    if ((slot != null) && slot.getHasStack()) {
      var itemstack1 = slot.getStack()
      itemstack = itemstack1.copy()
      if (i < inv.getSizeInventory()) {
        if (!mergeItemStack(itemstack1, inv.getSizeInventory(), inventorySlots.size(), false)) return ItemStack.EMPTY
      } else if (!mergeItemStack(itemstack1, 12, 15, false)) {
        if(i < inv.getSizeInventory + 9){
          if(!mergeItemStack(itemstack1, inv.getSizeInventory + 9, inv.getSizeInventory + 9 + 27, false)) return ItemStack.EMPTY
        } else {
          if(!mergeItemStack(itemstack1, inv.getSizeInventory, inv.getSizeInventory + 9, false)) return ItemStack.EMPTY
        }
      }
      if (itemstack1.getCount == 0) slot.putStack(new ItemStack(Items.AIR, 0))
      else slot.onSlotChanged()
    }
    itemstack
  }
}

class RucksackGui(player: EntityPlayer) extends BagGui(new RucksackContainer(player)) {
  def getMeta = ItemDefinitions.Backpack.RUCKSACK
  
  protected override def drawGuiContainerBackgroundLayer(partialTick: Float, mouseX: Int, mouseY: Int) {
    super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY)
    drawTexturedModalRect(guiLeft + 7, guiTop + 25, 0, 166, 162, 54)  
  }
}

class RucksackContainer(player: EntityPlayer) extends BagContainer(player) {
  for(i <- 0 until 27) getSlot(i).asInstanceOf[SlotWithDisable].enable

  override def getItemDamage: Int = ItemDefinitions.Backpack.RUCKSACK
  def getSize = 27
}