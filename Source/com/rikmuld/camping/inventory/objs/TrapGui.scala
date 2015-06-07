package com.rikmuld.camping.inventory.objs

import com.rikmuld.corerm.inventory.RMContainerTile
import com.rikmuld.corerm.client.GuiContainerSimple
import com.rikmuld.corerm.inventory.SlotOnlyItems
import net.minecraft.inventory.IInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.init.Blocks
import net.minecraft.inventory.Slot
import net.minecraft.block.Block
import com.rikmuld.camping.Lib._
import com.rikmuld.corerm.CoreUtils._

class GuiTrap(player: EntityPlayer, inv: IInventory) extends GuiContainerSimple(new ContainerTrap(player, inv)) {
  ySize = 120

  def getTexture: String = TextureInfo.GUI_TRAP
  def getName: String = ""
  def hasName: Boolean = false
}

class ContainerTrap(player: EntityPlayer, inv: IInventory) extends RMContainerTile(player, inv) {
  addSlotToContainer(new SlotOnlyItems(inv, 0, 80, 12))

  this.addSlots(player.inventory, 9, 3, 9, 8, 38)
  this.addSlots(player.inventory, 0, 1, 9, 8, 96)

  override def transferStackInSlot(player: EntityPlayer, slotNum: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(slotNum).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy
      if (slotNum < inv.getSizeInventory) {
        if (!mergeItemStack(itemstack1, inv.getSizeInventory, inventorySlots.size, true)) return null
      } else if (Block.getBlockFromItem(itemstack1.getItem()) != null || (!mergeItemStack(itemstack1, 0, inv.getSizeInventory, false))) return null
      if (itemstack1.stackSize == 0) {
        slot.putStack(null)
      } else {
        slot.onSlotChanged
      }
    }
    itemstack
  }
}