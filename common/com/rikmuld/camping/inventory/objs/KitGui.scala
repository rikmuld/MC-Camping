package com.rikmuld.camping.inventory.objs

import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.client.GuiContainerSimple
import com.rikmuld.corerm.inventory.RMContainerItem
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.inventory._
import net.minecraft.nbt.NBTTagList
import com.rikmuld.corerm.inventory.SlotNoPickup
import net.minecraft.item.ItemStack
import java.util.ArrayList
import net.minecraft.inventory.Slot
import net.minecraft.nbt.NBTTagCompound
import com.rikmuld.corerm.inventory.RMInventoryItem
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.camping.misc.CookingEquipment
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
  
class KitGui(player: EntityPlayer) extends GuiContainerSimple(new KitContainer(player)) {
  ySize = 181

  def getTexture: String = TextureInfo.GUI_KIT
  def getName: String = ""
  def hasName: Boolean = false
}

class KitContainer(player: EntityPlayer) extends RMContainerItem(player) {
  this.addSlots(inv, 0, 1, 5, 44, 16)
  this.addSlots(inv, 5, 2, 1, 44, 34)
  this.addSlots(inv, 7, 2, 1, 116, 34)
  this.addSlots(inv, 9, 1, 5, 44, 70)

  for (row <- 0 until 9) {
    if (row == player.inventory.currentItem) addSlotToContainer(new SlotNoPickup(player.inventory, row, 8 + (row * 18), 157))
    else addSlotToContainer(new Slot(player.inventory, row, 8 + (row * 18), 157))
  }
  this.addSlots(player.inventory, 9, 3, 9, 8, 99)

  override def getItemInv = new RMInventoryItem(player.inventory.getCurrentItem, player, 14, 1, true)
  override def getItem = Objs.kit
  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = null
    var slot = inventorySlots.get(i).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack()) {
      val itemstack1 = slot.getStack()
      itemstack = itemstack1.copy()
      val size = itemstack1.stackSize
      if (i < inv.getSizeInventory()) {
        if (!mergeItemStack(itemstack1, inv.getSizeInventory(), inventorySlots.size(), false)) return null
      } else {
        if(i < inv.getSizeInventory + 9){
          if(!mergeItemStack(itemstack1, inv.getSizeInventory + 9, inv.getSizeInventory + 9 + 27, false)) return null
        } else {
          if(!mergeItemStack(itemstack1, inv.getSizeInventory, inv.getSizeInventory + 9, false)) return null
        }
      }
      if (itemstack1.stackSize == 0) slot.putStack(null)
      else slot.onSlotChanged()
    }
    itemstack
  }
  override def onContainerClosed(player: EntityPlayer) {
    super.onContainerClosed(player)
    if (!player.worldObj.isRemote) {
      inv.closeInventory(player)
      if (player.inventory.getCurrentItem != null && player.inventory.getCurrentItem.getItem == Objs.kit) {
        val items = new ArrayList[ItemStack]
        val containingItems = inv.asInstanceOf[RMInventoryItem].tag.getTag("Items").asInstanceOf[NBTTagList]
        for (itemCound <- 0 until containingItems.tagCount) {
          items.add(ItemStack.loadItemStackFromNBT(containingItems.getCompoundTagAt(itemCound).asInstanceOf[NBTTagCompound]))
        }
                
        if (CookingEquipment.getCookingForRecipe(items) != null) {
          player.setCurrentItem(CookingEquipment.getCookingForRecipe(items).itemInfo)
        } else if (items.size > 0) {
          player.setCurrentItem(new ItemStack(Objs.kit, 1, Kit.USELESS))
        } else {
          player.setCurrentItem(new ItemStack(Objs.kit, 1, Kit.EMPTY))
        }
        inv.asInstanceOf[RMInventoryItem].setNBT(player.inventory.getCurrentItem)
      }
    }
  }
}