package com.rikmuld.camping.common.inventory.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import net.minecraft.inventory.Container
import java.util.ArrayList
import net.minecraft.inventory.Slot
import com.rikmuld.camping.core.Utils._
import net.minecraft.inventory.SlotCrafting
import net.minecraft.inventory.InventoryCraftResult
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.inventory.IInventory
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.common.util.Constants
import scala.collection.JavaConversions._
import java.util.Random
import com.rikmuld.camping.common.inventory.inventory.InventoryItemMain
import com.rikmuld.camping.common.inventory.inventory.InventoryPlayerMain
import com.rikmuld.camping.common.inventory.SlotDisable
import com.rikmuld.camping.common.inventory.SlotItemsNot
import com.rikmuld.camping.common.inventory.SlotItemsOnly
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.common.inventory.SlotItemsNot
import com.rikmuld.camping.common.inventory.SlotDisable
import com.rikmuld.camping.common.inventory.inventory.InventoryItemMain
import com.rikmuld.camping.common.inventory.SlotItemsOnly
import com.rikmuld.camping.common.inventory.inventory.InventoryPlayerMain

class ContainerCampinv(var player: EntityPlayer) extends Container {
  var backpackInv: InventoryItemMain = new InventoryItemMain(new ItemStack(Objs.backpack, 1, 0), player, 27, 64)
  var slots: ArrayList[SlotDisable] = new ArrayList[SlotDisable]()
  var campinv: InventoryCampinv = new InventoryCampinv(player, slots, backpackInv)

  for (row <- 0 until 3; collom <- 0 until 9) {
    val slot = new SlotItemsNot(backpackInv, collom + (row * 9), 30 + (collom * 18), 20 + (row * 18), Objs.backpack)
    slot.disable
    addSlotToContainer(slot)
    slots.add(slot)
  }

  addSlotToContainer(new SlotItemsOnly(campinv, 0, 8, 28, new ItemStack(Objs.backpack)))
  addSlotToContainer(new SlotItemsOnly(campinv, 1, 8, 46, Objs.knife))
  addSlotToContainer(new SlotItemsOnly(campinv, 2, 196, 28, new ItemStack(Objs.lantern)))
  addSlotToContainer(new SlotItemsOnly(campinv, 3, 196, 46, Items.filled_map))

  this.addSlots(player.inventory, 0, 1, 9, 30, 136)
  this.addSlots(player.inventory, 9, 3, 9, 30, 78)

  campinv.openInventory()
  backpackInv.openInventory()

  override def canInteractWith(player: EntityPlayer): Boolean = !player.isDead
  override def onContainerClosed(player: EntityPlayer) {
    super.onContainerClosed(player)
    campinv.closeInventory()
    backpackInv.closeInventory()
  }
  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(i).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (i < 28) {
        if (!mergeItemStack(itemstack1, 28, inventorySlots.size, true)) return null
      } else {
        if (campinv.getStackInSlot(0) == null) {
          if (itemstack1.getItem != Objs.backpack ||
            !mergeItemStack(itemstack1, backpackInv.getSizeInventory, backpackInv.getSizeInventory + campinv.getSizeInventory,
              false)) return null
        } else {
          if (!mergeItemStack(itemstack1, 0, backpackInv.getSizeInventory, false)) return null
        }
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

class ContainerCampinvCraft(var player: EntityPlayer) extends Container {
  var backpackInv: InventoryItemMain = new InventoryItemMain(new ItemStack(Objs.backpack, 1, 0), player, 27, 64)
  var slots: ArrayList[SlotDisable] = new ArrayList[SlotDisable]()
  var campinv: InventoryCampinv = new InventoryCampinv(player, slots, backpackInv)
  var craftMatrix: InventoryCrafting = new InventoryCrafting(this, 3, 3)
  var craftResult: IInventory = new InventoryCraftResult()

  for (row <- 0 until 3; collom <- 0 until 9) {
    val slot = new SlotDisable(backpackInv, collom + (row * 9), 30 + (collom * 18), 20 + (row * 18))
    slot.disable
    slots.add(slot)
  }

  addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 111, 44))
  this.addSlots(craftMatrix, 0, 3, 3, 17, 26)
  addSlotToContainer(new SlotItemsOnly(campinv, 1, 143, 44, Objs.knife))
  this.addSlots(player.inventory, 0, 1, 9, 8, 151)
  this.addSlots(player.inventory, 9, 3, 9, 8, 93)

  campinv.openInventory()
  backpackInv.openInventory()

  this.onCraftMatrixChanged(this.craftMatrix)

  override def onCraftMatrixChanged(par1IInventory: IInventory) = this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance.findMatchingRecipe(this.craftMatrix, this.player.worldObj))
  override def canInteractWith(player: EntityPlayer): Boolean = !player.isDead
  override def onContainerClosed(player: EntityPlayer) {
    super.onContainerClosed(player)
    if (!this.player.worldObj.isRemote) {
      for (i <- 0 until 9) {
        val itemstack = this.craftMatrix.getStackInSlotOnClosing(i)
        if (itemstack != null) {
          player.dropPlayerItemWithRandomChoice(itemstack, false)
        }
      }
    }
    campinv.closeInventory()
  }
  override def func_94530_a(par1ItemStack: ItemStack, par2Slot: Slot): Boolean = par2Slot.inventory != this.craftResult && super.func_94530_a(par1ItemStack, par2Slot)
  override def transferStackInSlot(p: EntityPlayer, i: Int): ItemStack = return null
}

object InventoryCampinv {
  def dropItems(player: EntityPlayer) {
    if (player.getEntityData.hasKey("campInv") == false) return
    val tag = player.getEntityData.getCompoundTag("campInv")
    val inventory = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND)
    for (i <- 0 until inventory.tagCount()) {
      val Slots = inventory.getCompoundTagAt(i).asInstanceOf[NBTTagCompound]
      Slots.getByte("Slot")
      player.worldObj.dropItemsInWorld(Array(ItemStack.loadItemStackFromNBT(Slots)), player.posX.toInt, player.posY.toInt, player.posZ.toInt, new Random())
    }
  }
}

class InventoryCampinv(player: EntityPlayer, var slots: ArrayList[SlotDisable], var backpack: InventoryItemMain) extends InventoryPlayerMain(player, 4) {
  if (player.getEntityData.hasKey("campInv") == false) player.getEntityData.setTag("campInv", new NBTTagCompound())
  tag = player.getEntityData.getCompoundTag("campInv")

  override def onChange(slotNum: Int) {
    super.onChange(slotNum)
    if (slotNum == 0) {
      if (getStackInSlot(0) != null) {
        for (slot <- slots) {
          slot.enable
          if (!getStackInSlot(0).hasTagCompound()) getStackInSlot(0).setTagCompound(new NBTTagCompound())
          backpack.tag = getStackInSlot(0).getTagCompound
          backpack.readFromNBT(backpack.tag)
        }
        backpack.item = getStackInSlot(0).getItem
      } else {
        for (slot <- slots) {
          slot.disable
          backpack.tag = new NBTTagCompound()
          backpack.readFromNBT(backpack.tag)
        }
      }
    }
  }
  override def openInventory() {
    super.openInventory()
    onChange(0)
  }
}