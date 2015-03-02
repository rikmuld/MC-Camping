package com.rikmuld.camping.common.inventory.gui

import java.util.ArrayList
import java.util.Random
import scala.collection.JavaConversions._
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.Utils._
import com.rikmuld.corerm.common.inventory.SlotDisable
import com.rikmuld.corerm.common.inventory.SlotItemsNot
import com.rikmuld.corerm.common.inventory.SlotItemsOnly
import com.rikmuld.corerm.common.inventory.inventory.InventoryItemMain
import com.rikmuld.corerm.common.inventory.inventory.InventoryPlayerMain
import com.rikmuld.corerm.core.CoreUtils._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryCraftResult
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.inventory.Slot
import net.minecraft.inventory.SlotCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import com.rikmuld.camping.common.inventory.SlotTabbedItemsNot
import net.minecraft.client.Minecraft
import com.rikmuld.corerm.common.inventory.SlotWithDisable
import com.rikmuld.camping.common.inventory.SlotTabbedCrafting
import com.rikmuld.camping.common.inventory.SlotTabbed

class ContainerCampinv(player:EntityPlayer) extends Container with ContainerTabbed {
  var backpackInv: InventoryItemMain = new InventoryItemMain(new ItemStack(Objs.backpack, 1, 0), player, 27, 64)
  var slots: ArrayList[SlotWithDisable] = new ArrayList[SlotWithDisable]()
  var campinv: InventoryCampinv = new InventoryCampinv(player, slots, backpackInv)
  var craftMatrix: InventoryCrafting = new InventoryCrafting(this, 3, 3)
  var craftResult: IInventory = new InventoryCraftResult()
 
  this.addSlots(player.inventory, 0, 1, 9, 30, 142)
  this.addSlots(player.inventory, 9, 3, 9, 30, 84)
  
  addSlotToContainer(new SlotItemsOnly(campinv, 0, 8, 35, new ItemStack(Objs.backpack)))
  addSlotToContainer(new SlotItemsOnly(campinv, 1, 8, 53, Objs.knife))
  addSlotToContainer(new SlotItemsOnly(campinv, 2, 196, 35, new ItemStack(Objs.lantern)))
  addSlotToContainer(new SlotItemsOnly(campinv, 3, 196, 53, Items.filled_map))
  
  for (row <- 0 until 3; collom <- 0 until 9) {
    val slot = new SlotTabbedItemsNot(backpackInv, collom + (row * 9), 30 + (collom * 18), 17 + (row * 18), 1, 0, Objs.backpack)
    slot.disable
    addSlotToContainer(slot)
    slots.add(slot)
  }

  addSlotToContainer(new SlotTabbedCrafting(player, craftMatrix, craftResult, 0, 147, 35, 2, 0))  
  for (row <- 0 until 3; collom <- 0 until 3) addSlotToContainer(new SlotTabbed(craftMatrix, collom + (row * 3), 53 + (collom * 18), 17 + (row * 18), 2, 0))
    
  updateTab(player, 0, 0)
  onCraftMatrixChanged(this.craftMatrix)
  campinv.openInventory()
  backpackInv.openInventory()
  
  override def onCraftMatrixChanged(par1IInventory: IInventory) = this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance.findMatchingRecipe(this.craftMatrix, this.player.worldObj))
  override def onContainerClosed(player: EntityPlayer) {
    if (!this.player.worldObj.isRemote) {
      for (i <- 0 until 9) {
        val itemstack = this.craftMatrix.getStackInSlotOnClosing(i)
        if (itemstack != null) {
          player.dropPlayerItemWithRandomChoice(itemstack, false)
        }
      }
    }
    campinv.closeInventory
    backpackInv.closeInventory
    super.onContainerClosed(player)
  }
  override def canInteractWith(player: EntityPlayer): Boolean = !player.isDead
  override def transferStackInSlot(player: EntityPlayer, slotNum: Int): ItemStack = {
    var itemstack: ItemStack = null
    val slot = inventorySlots.get(slotNum).asInstanceOf[Slot]
    if ((slot != null) && slot.getHasStack) {
      val itemstack1 = slot.getStack
      itemstack = itemstack1.copy()
      if (slotNum >= 36) {
        if (!mergeItemStack(itemstack1, 0, inventorySlots.size, false)) return null
      } else {
        if(itemstack.getItem==Objs.backpack){
          if (!mergeItemStack(itemstack1, 36, 37, false)) return null
        } else if(itemstack.getItem==Objs.knife){
          if(!mergeItemStack(itemstack1, 37, 38, false)) return null
        } else if(itemstack.isItemEqual(new ItemStack(Objs.lantern))){
          if(!mergeItemStack(itemstack1, 38, 39, false)) return null
        } else if(itemstack.getItem==Items.filled_map){
          if(!mergeItemStack(itemstack1, 39, 40, false)) return null
        }
        else return null
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

class InventoryCampinv(player: EntityPlayer, var slots: ArrayList[SlotWithDisable], var backpack: InventoryItemMain) extends InventoryPlayerMain(player, 4) {
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