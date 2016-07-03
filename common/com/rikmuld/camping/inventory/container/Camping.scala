package com.rikmuld.camping.inventory.container

import com.rikmuld.camping.objs.Objs
import net.minecraft.inventory.InventoryCraftResult
import net.minecraft.inventory.IInventory
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.inventory.SlotItemsOnly
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.item.ItemStack
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.init.Items
import net.minecraft.inventory.Container
import java.util.ArrayList
import net.minecraft.inventory.Slot
import com.rikmuld.corerm.inventory.SlotWithDisable
import net.minecraft.item.crafting.CraftingManager
import java.util.Random
import net.minecraftforge.common.util.Constants
import com.rikmuld.corerm.inventory.RMPlayerInventory
import com.rikmuld.corerm.inventory.RMInventoryItem
import scala.collection.JavaConversions._
import com.rikmuld.camping.objs.BlockDefinitions._
import net.minecraft.inventory.ContainerPlayer
import net.minecraft.entity.player.InventoryPlayer
import com.rikmuld.camping.inventory.gui.GuiCampinginv
import com.rikmuld.camping.Utils._
import net.minecraft.init.Blocks
import net.minecraft.item.ItemArmor
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.corerm.tabbed.SlotTabbedCrafting
import com.rikmuld.corerm.tabbed.SlotTabbedItemsNot
import com.rikmuld.corerm.tabbed.SlotTabbed
import com.rikmuld.corerm.tabbed.ContainerTabbed
import com.rikmuld.camping.objs.BlockDefinitions
import com.rikmuld.camping.objs.ItemDefinitions
import net.minecraft.inventory.EntityEquipmentSlot

class ContainerCampinv(player:EntityPlayer) extends Container with ContainerTabbed {
  var backpackInv: RMInventoryItem = new RMInventoryItem(new ItemStack(Objs.backpack, 1, 0), player, 27, 64, false)
  var slots: ArrayList[SlotWithDisable] = new ArrayList[SlotWithDisable]()
  var campinv: InventoryCampinv = new InventoryCampinv(player, slots, backpackInv)
  var craftMatrix: InventoryCrafting = new InventoryCrafting(this, 3, 3)
  var craftMatrixSmall: InventoryCrafting = new InventoryCrafting(this, 2, 2)
  var craftResult: IInventory = new InventoryCraftResult()
  var craftResultSmall: IInventory = new InventoryCraftResult()
  
  this.addSlots(player.inventory, 0, 1, 9, 30, 142)
  this.addSlots(player.inventory, 9, 3, 9, 30, 84)
    
  addSlotToContainer(new SlotItemsOnly(campinv, 0, 8, 35, Objs.backpack))
  addSlotToContainer(new SlotItemsOnly(campinv, 1, 8, 53, Objs.knife))
  addSlotToContainer(new SlotItemsOnly(campinv, 2, 196, 35, new ItemStack(Objs.lantern, 1, BlockDefinitions.Lantern.ON)))
  addSlotToContainer(new SlotItemsOnly(campinv, 3, 196, 53, Items.FILLED_MAP))
  
  for (row <- 0 until 3; collom <- 0 until 9) {
    val slot = new SlotTabbedItemsNot(backpackInv, collom + (row * 9), 30 + (collom * 18), 17 + (row * 18), 1, 0, Objs.backpack)
    slot.disable
    addSlotToContainer(slot)
    slots.add(slot)
  }

  addSlotToContainer(new SlotTabbedCrafting(player, craftMatrix, craftResult, 0, 147, 35, 2, 0))  
  addSlotToContainer(new SlotTabbedCrafting(player, craftMatrixSmall, craftResultSmall, 0, 176, 28, 0, 0))  
  for (row <- 0 until 3; collom <- 0 until 3) addSlotToContainer(new SlotTabbed(craftMatrix, collom + (row * 3), 53 + (collom * 18), 17 + (row * 18), 2, 0))
  for (row <- 0 until 2; collom <- 0 until 2) addSlotToContainer(new SlotTabbed(craftMatrixSmall, collom + (row * 2), 120 + (collom * 18), 18 + (row * 18), 0, 0))

  val armorSlots = List(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET)
  for (i <- 0 until 4) {
      this.addSlotToContainer(new SlotTabbed(player.inventory, 36 + (3 - i), 30, 8 + i * 18, 0, 0){
          override def getSlotStackLimit =  1
          override def isItemValid(stack:ItemStack):Boolean = {
              if (stack == null) return false;
              return stack.getItem().isValidArmor(stack, armorSlots(i), player)
          }
          @SideOnly(Side.CLIENT)
          override def getSlotTexture  = ItemArmor.EMPTY_SLOT_NAMES(3-i)
      })
  }
  
  addSlotToContainer(new SlotTabbed(player.inventory, 40, 99, 62, 0, 0) {
    @SideOnly(Side.CLIENT)
    override def getSlotTexture = "minecraft:items/empty_armor_slot_shield";
  })
  
  updateTab(player, 0, 0)
  onCraftMatrixChanged(this.craftMatrix)
  campinv.openInventory(player)
  backpackInv.openInventory(player)
  override def updateTab(player:EntityPlayer, left:Int, top:Int){
    super.updateTab(player, left, top)
    campinv.backpackChange()
  }
  override def onCraftMatrixChanged(par1IInventory: IInventory) {
    craftResult.setInventorySlotContents(0, CraftingManager.getInstance.findMatchingRecipe(this.craftMatrix, this.player.worldObj))
    craftResultSmall.setInventorySlotContents(0, CraftingManager.getInstance.findMatchingRecipe(this.craftMatrixSmall, this.player.worldObj))
  }
  override def onContainerClosed(player: EntityPlayer) {
    if (!this.player.worldObj.isRemote) {
      for (i <- 0 until 9) {
        val itemstack = craftMatrix.removeStackFromSlot(i)
        if (itemstack != null) player.dropItem(itemstack, false)
        if(i<4){
          val itemstack2 = craftMatrixSmall.removeStackFromSlot(i)
          if (itemstack2 != null) player.dropItem(itemstack2, false)
        }
      }
    }
    campinv.closeInventory(player)
    backpackInv.closeInventory(player)
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
        if (!this.mergeItemStack(itemstack1, 0, 36, false))return null
        if(slotNum == 67||slotNum==68)slot.onSlotChange(itemstack1, itemstack)
      } else if(slotNum < 36){
        var success = false
        if(itemstack.getItem==Objs.backpack) {
          if(mergeItemStack(itemstack1, 36, 37, false))success = true
        }
        else if(itemstack.getItem==Objs.knife) {
          if(mergeItemStack(itemstack1, 37, 38, false))success = true
        }
        else if(itemstack.isItemEqual(new ItemStack(Objs.lantern, 0, BlockDefinitions.Lantern.ON))) {
          if(mergeItemStack(itemstack1, 38, 39, false))success = true
        }
        else if(itemstack.getItem==Items.FILLED_MAP){
          if(mergeItemStack(itemstack1, 39, 40, false))success = true
        }
        else if(getCurrentTabTop == GuiCampinginv.TAB_ARMOR && itemstack.getItem.isInstanceOf[ItemArmor]){
          if(mergeItemStack(itemstack1, 82, 86, false))success = true
        }
        if(!success){
          success = false
          if(slotNum < 36 && getCurrentTabTop == GuiCampinginv.TAB_BACKPACK){
            val slots = campinv.getEnabledBackpackSlots()
            slots.foreach { slot =>  
              if(!success&&this.mergeItemStack(itemstack1, 4 + 36 + slot, 4 + 36 + slot + 1, false))success = true    
            }
          }
          if (slotNum < 9) {
            if (!success && !this.mergeItemStack(itemstack1, 9, 36, false))return null
          } else if (slotNum >= 9 && slotNum < 36) {
            if (!success && !this.mergeItemStack(itemstack1, 0, 9, false))return null
          }
        }
      } else return null
      if (itemstack1.stackSize == 0) {
        slot.putStack(null)
      } else {
        slot.onSlotChanged()
      }
      if (itemstack1.stackSize == itemstack.stackSize)return null
      slot.onPickupFromSlot(player, itemstack1)
    }
    itemstack
  }
}

object InventoryCampinv {
  final val POUCH = List(12, 13, 14)
  final val BACKPACK = List(3, 4, 5, 12, 13, 14, 21, 22, 23)
  final val RUCKSACK = List.range(0, 27)
  
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

class InventoryCampinv(player: EntityPlayer, var slots: ArrayList[SlotWithDisable], var backpack: RMInventoryItem) extends RMPlayerInventory(player, 4) {
  if (player.getEntityData.hasKey("campInv") == false) player.getEntityData.setTag("campInv", new NBTTagCompound())
  tag = player.getEntityData.getCompoundTag("campInv")

  final val SLOT_BACKPACK = 0
  
  def getEnabledBackpackSlots():List[Int] = {
    val pack = getStackInSlot(SLOT_BACKPACK)
    if(pack != null){
      pack.getItemDamage match {
        case ItemDefinitions.Backpack.POUCH => InventoryCampinv.POUCH
        case ItemDefinitions.Backpack.BACKPACK => InventoryCampinv.BACKPACK
        case ItemDefinitions.Backpack.RUCKSACK => InventoryCampinv.RUCKSACK
      }
    } else List[Int]()
  }
  override def onChange(slotNum: Int) {
    super.onChange(slotNum)
    if (slotNum == SLOT_BACKPACK) {
      backpackChange()
    }
    if(getInventory.filter { stack => stack==null }.size == 0)player.addStat(Objs.achCamperFull)
  }
  override def openInventory(player:EntityPlayer) {
    super.openInventory(player)
    onChange(SLOT_BACKPACK)
  }
  override def getInventoryStackLimit = 1
  def backpackChange(){
    var pack = getStackInSlot(SLOT_BACKPACK)
    if (pack != null) {
      if (!pack.hasTagCompound()) pack.setTagCompound(new NBTTagCompound())
      backpack.tag = pack.getTagCompound
      backpack.readFromNBT(backpack.tag)
      
      enableBackpackSlots(pack.getItemDamage)
      
      backpack.item = pack.getItem
    } else {
      backpack.tag = new NBTTagCompound()
      backpack.readFromNBT(backpack.tag)
      
      disdableBackpackSlots()
    }
  }
  def enableBackpackSlots(damage:Int){
    var set:List[Int] = List()
    damage match {
      case ItemDefinitions.Backpack.POUCH => set = InventoryCampinv.POUCH
      case ItemDefinitions.Backpack.BACKPACK => set = InventoryCampinv.BACKPACK
      case ItemDefinitions.Backpack.RUCKSACK => set = InventoryCampinv.RUCKSACK
    }
    for (slot <- slots) slot.disable
    for(slot <- set) slots(slot).enable
  }
  def disdableBackpackSlots(){
    for (slot <- slots) slot.disable
  }
}