package com.rikmuld.camping.common.inventory.gui

import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.init.Items
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.common.inventory.SlotItemsOnly
import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook
import com.rikmuld.camping.common.inventory.SlotCooking
import net.minecraft.inventory.ICrafting
import com.rikmuld.camping.core.ObjRegistry
import java.util.ArrayList
import com.rikmuld.camping.core.Objs

class ContainerCampfire(player: EntityPlayer, tile: IInventory) extends ContainerTileMain(player, tile) {
  override def addContainer {
    addSlotToContainer(new SlotItemsOnly(tile, 0, 71, 12, Items.dye))
    this.addSlots(player.inventory, 0, 1, 9, 8, 96)
    this.addSlots(player.inventory, 9, 3, 9, 8, 38)
  }
  override def transferStackInSlot(player: EntityPlayer, slotNum: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(slotNum).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (slotNum < tile.getSizeInventory) {
        if (!mergeItemStack(itemstack1, tile.getSizeInventory, inventorySlots.size, true)) return null
      } else {
        if (itemstack.getItem == Items.dye) {
          if (!mergeItemStack(itemstack1, 0, 1, false)) return null
        } else return null
      }
      if (itemstack1.stackSize == 0) {
        slot.putStack(null)
      } else {
        slot.onSlotChanged()
      }
    }
    itemstack
  }
}

class ContainerCampfireCook(player: EntityPlayer, tile: IInventory) extends ContainerTileMain(player, tile) {
  private var fire: TileEntityCampfireCook = _
  var slots:ArrayList[SlotCooking] = _
  
  override def addContainer {
    fire = tile.asInstanceOf[TileEntityCampfireCook]
    slots = new ArrayList[SlotCooking]()
    
    addSlotToContainer(new SlotItemsOnly(tile, 0, 80, 84, new ItemStack(Items.coal)))
    addSlotToContainer(new SlotItemsOnly(tile, 1, 150, 9, new ItemStack(Objs.kit, 1, 1), new ItemStack(Objs.kit, 1, 3), new ItemStack(Objs.kit, 1, 2)))
    for (i <- 0 until 10) {
      val slot = new SlotCooking(tile, i + 2, 0, 0)
      slots.add(slot)
      addSlotToContainer(slot)
    }
    fire.setSlots(slots)
    this.addSlots(player.inventory, 0, 1, 9, 8, 164)
    this.addSlots(player.inventory, 9, 3, 9, 8, 106)
  }
  override def addCraftingToCrafters(crafting: ICrafting) {
    super.addCraftingToCrafters(crafting)
    for (i <- 0 until fire.cookProgress.length) {
      crafting.sendProgressBarUpdate(this, i, fire.cookProgress(i))
    }
  }
  override def transferStackInSlot(player: EntityPlayer, slotNum: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(slotNum).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (slotNum < fire.getSizeInventory) {
        if (!mergeItemStack(itemstack1, fire.getSizeInventory, inventorySlots.size, true)) return null
      } else {
        if (itemstack.getItem == Items.coal) {
          if (!mergeItemStack(itemstack1, 0, 1, false)) return null
        } else if (itemstack.getItem == Objs.kit) {
          if (!mergeItemStack(itemstack1, 1, 2, false)) return null
        } else return null
      }
      if (itemstack1.stackSize == 0) {
        slot.putStack(null)
      } else {
        slot.onSlotChanged()
      }
    }
    itemstack
  }
}
