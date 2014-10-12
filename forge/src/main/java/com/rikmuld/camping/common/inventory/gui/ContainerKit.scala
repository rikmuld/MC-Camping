package com.rikmuld.camping.common.inventory.gui

import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.common.inventory.inventory.InventoryItemMain
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.common.inventory.SlotNoPickup
import net.minecraft.inventory.Slot
import net.minecraft.nbt.NBTTagList
import com.rikmuld.camping.core.ObjRegistry
import net.minecraft.item.ItemStack
import com.rikmuld.camping.misc.CookingEquipment
import java.util.ArrayList
import net.minecraft.nbt.NBTTagCompound
import com.rikmuld.camping.core.KitInfo

class ContainerKit(player: EntityPlayer) extends ContainerItemMain(player) {
  override def addContainer {
    this.addSlots(inv, 0, 1, 5, 44, 16)
    this.addSlots(inv, 5, 2, 1, 44, 34)
    this.addSlots(inv, 7, 2, 1, 116, 34)
    this.addSlots(inv, 9, 1, 5, 44, 70)
    this.addSlots(player.inventory, 9, 3, 9, 8, 99)

    for (row <- 0 until 9) {
      if (row == player.inventory.currentItem) addSlotToContainer(new SlotNoPickup(player.inventory, row, 8 + (row * 18), 157))
      else addSlotToContainer(new Slot(player.inventory, row, 8 + (row * 18), 157))
    }
  }
  override def getItemInv = new InventoryItemMain(player.getCurrentEquippedItem, player, 14, 1)
  override def getItem = Objs.kit
  override def onContainerClosed(player: EntityPlayer) {
    super.onContainerClosed(player)
    if (!player.worldObj.isRemote) {
      inv.closeInventory()
      if (player.getCurrentEquippedItem != null && 
        player.getCurrentEquippedItem.getItem == Objs.kit) {
        val items = new ArrayList[ItemStack]()
        val containingItems = inv.asInstanceOf[InventoryItemMain].tag.getTag("Items").asInstanceOf[NBTTagList]
        for (itemCound <- 0 until containingItems.tagCount()) {
          items.add(ItemStack.loadItemStackFromNBT(containingItems.getCompoundTagAt(itemCound).asInstanceOf[NBTTagCompound]))
        }
        if (CookingEquipment.getCookingForRecipe(items) != null) {
          player.setCurrentItem(CookingEquipment.getCookingForRecipe(items).itemInfo)
        } else if (items.size > 0) {
          player.setCurrentItem(new ItemStack(Objs.kit, 1, KitInfo.KIT_USELESS))
        } else {
          player.setCurrentItem(new ItemStack(Objs.kit, 1, KitInfo.KIT_EMPTY))
        }
        inv.asInstanceOf[InventoryItemMain].setNBT(player.getCurrentEquippedItem)
      }
    }
  }
}