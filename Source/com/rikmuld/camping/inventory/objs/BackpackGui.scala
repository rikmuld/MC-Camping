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

class BackpackGui(player: EntityPlayer) extends GuiContainerSimple(new BackpackContainer(player)) {
  override def getTexture: String = TextureInfo.GUI_BAG;
  override def getName: String = "Hiking Bag";
  override def hasName: Boolean = true;
}

class BackpackContainer(player: EntityPlayer) extends RMContainerItem(player) {
  for (row <- 0 until 3; collom <- 0 until 9) this.addSlot(new SlotItemsNot(inv, collom + (row * 9), 8 + (collom * 18), 26 + (row * 18), Objs.backpack))
  this.addSlots(invPlayer, 9, 3, 9, 8, 84)
  for (row <- 0 until 9) {
    if (row == invPlayer.currentItem) addSlotToContainer(new SlotNoPickup(invPlayer, row, 8 + (row * 18), 142))
    else addSlotToContainer(new Slot(invPlayer, row, 8 + (row * 18), 142))
  }

  override def getItemInv = new RMInventoryItem(player.getCurrentEquippedItem, player, 27, 64, true)
  override def getItem: Item = Objs.backpack
}