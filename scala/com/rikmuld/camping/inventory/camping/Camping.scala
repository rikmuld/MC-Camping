package com.rikmuld.camping.inventory.camping

import com.rikmuld.camping.Lib.{NBTInfo, TextureInfo}
import com.rikmuld.camping.inventory._
import com.rikmuld.camping.inventory.camping.InventoryCamping._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.corerm.features.tabbed.ContainerTabbed
import com.rikmuld.corerm.inventory.container.ContainerSimple
import com.rikmuld.corerm.inventory.inventory.{InventoryItem, InventoryPlayer}
import com.rikmuld.corerm.utils.NBTUtil
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.init.Items
import net.minecraft.inventory._
import net.minecraft.item.{Item, ItemArmor, ItemStack}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class ContainerCamping(player:EntityPlayer) extends ContainerSimple[InventoryCamping](player) with ContainerTabbed {
  var backpackSlots: Seq[SlotTabbedBackpack] =
    _

  var backpackSlot: SlotItem =
    _

  override def playerInvX: Int =
    30

  def getID: String =
    "camping_inventory"

  override def addInventorySlots(): Unit = {
    backpackSlot = new SlotItem(getIInventory, 0, 8, 35, Objs.backpack)

    addSlotToContainer(backpackSlot)
    addSlotToContainer(new SlotItem(getIInventory, 1, 8, 53, Objs.knife))
    addSlotToContainer(new SlotItem(getIInventory, 2, 196, 35, Item.getItemFromBlock(Objs.lantern)))
    addSlotToContainer(new SlotItem(getIInventory, 3, 196, 53, Items.FILLED_MAP))

    backpackSlots = for (row <- 0 until 3; collom <- 0 until 9)
        yield new SlotTabbedBackpack(collom + (row * 9), 30 + (collom * 18), 17 + (row * 18), 0, 1)

    backpackSlots.foreach(slot => addSlotToContainer(slot))

    addSlotToContainer(new SlotTabbedCrafting(player, getIInventory.craftMatrix, getIInventory.craftResult, 0, 147, 35, 0, 2))
    addSlotToContainer(new SlotTabbedCrafting(player, getIInventory.craftMatrixSmall, getIInventory.craftResultSmall, 0, 176, 28, 0, 0))

    for (row <- 0 until 3; collom <- 0 until 3)
      addSlotToContainer(new SlotTabbed(getIInventory.craftMatrix, collom + (row * 3), 53 + (collom * 18), 17 + (row * 18), 0, 2))

    for (row <- 0 until 2; collom <- 0 until 2)
      addSlotToContainer(new SlotTabbed(getIInventory.craftMatrixSmall, collom + (row * 2), 120 + (collom * 18), 18 + (row * 18), 0, 0))

    for (i <- 0 until 4)
      this.addSlotToContainer(new SlotTabbedArmor(player.inventory, player, 36 + (3 - i), 30, 8 + i * 18, 0, 0, i))

    addSlotToContainer(new SlotTabbed(player.inventory, 40, 99, 62, 0, 0) {
      @SideOnly(Side.CLIENT)
      override def getSlotTexture = TextureInfo.MC_INVENTORY_SHIELD
    })

    onCraftMatrixChanged(null)
  }

  override def initIInventory: InventoryCamping =
    new InventoryCamping(player, this)

  override def onCraftMatrixChanged(inv: IInventory) {
    slotChangedCraftingGrid(player.world, player, getIInventory.craftMatrix, getIInventory.craftResult)
    slotChangedCraftingGrid(player.world, player, getIInventory.craftMatrixSmall, getIInventory.craftResultSmall)
  }

  override def slotClick(slotId: Int, dragType: Int, clickTypeIn: ClickType, player: EntityPlayer): ItemStack = {
    if(slotId == backpackSlot.getSlotIndex) {
      getIInventory.getBackpack.foreach(_.closeInventory(player))
    }

    super.slotClick(slotId, dragType, clickTypeIn, player)
  }

  override def mergeFromInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean = {
    val result = super.mergeFromInventory(stack, original, index)

    if(result && (index == 31 || index == 32))
      getSlot(index).onSlotChange(stack, original)

    result
  }

  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean = {
    var success = stack.getItem match {
      case item if item == Objs.backpack => mergeItemStack(stack, 0, 1, false)
      case item if item == Objs.knife => mergeItemStack(stack, 1, 2, false)
      case item if item == Item.getItemFromBlock(Objs.lantern) => mergeItemStack(stack, 2, 3, false)
      case item if item == Items.FILLED_MAP => mergeItemStack(stack, 3, 4, false)
      case _ => false
    }

    if(!success && getCurrentTabTop == GuiCamping.TAB_ARMOR && stack.getItem.isInstanceOf[ItemArmor]){
      success = mergeItemStack(stack, 46, 50, false)
    } else if(!success && index >= inventorySlots.size - 36 && getCurrentTabTop == GuiCamping.TAB_BACKPACK) {
      success = getBackpackSlots.exists(slot =>
        this.mergeItemStack(stack, slot + 4, slot + 5, false)
      )
    }

    success
  }

  override def postSlotTransfer(moveStack: ItemStack, original: ItemStack, index: Int): Option[ItemStack] = {
    if (index == 31 || index == 32) {
      if (moveStack.getCount == original.getCount) return Some(ItemStack.EMPTY)
      else player.dropItem(getSlot(index).onTake(player, moveStack), false)
    }

    None
  }

  def backpackChanged(backpack: ItemStack, inv: Option[InventoryItem]): Unit = {
    backpackSlots.foreach(slot => {
      slot.disable()
      slot.setIInventory(inv)
    })

    for (slot <- getSlotsFor(backpack))
      backpackSlots(slot).enable()
  }

  def getBackpackSlots: Seq[Int] =
    getSlotsFor(getIInventory.getStackInSlot(SLOT_BACKPACK))

  override def updateTab(player: EntityPlayer, idLeft: Int, idTop: Int): Unit = {
    super.updateTab(player, idLeft, idTop)

    val backpackInv = getIInventory.getBackpack
    backpackChanged(backpackInv.fold(ItemStack.EMPTY)(_.getStack), getIInventory.getBackpack)
  }

  override def canMergeSlot(stack: ItemStack, slotIn: Slot): Boolean =
    (slotIn.inventory != getIInventory.craftResult) &&
      (slotIn.inventory != getIInventory.craftResultSmall) &&
      super.canMergeSlot(stack, slotIn)
}

object InventoryCamping {
  final val POUCH = 12 until 15
  final val BACKPACK = Vector(3, 4, 5, 12, 13, 14, 21, 22, 23)
  final val RUCKSACK = 0 until 27

  final val SLOT_BACKPACK = 0
  final val SLOT_KNIFE = 1
  final val SLOT_LANTERN = 2
  final val SLOT_MAP = 3

  def getSlotsFor(stack: ItemStack): Seq[Int] =
    if(stack.isEmpty || stack.getItem != Objs.backpack) Vector()
    else if(stack.getItemDamage == 0) POUCH
    else if(stack.getItemDamage == 1) BACKPACK
    else RUCKSACK

  def dropItems(player: EntityPlayer): Unit =
    if(player.getEntityData.hasKey(NBTInfo.INV_CAMPING))
      NBTUtil.readInventory(player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)).values foreach {
        item => player.dropItem(item, true, false)
      }
}

class InventoryCamping(player: EntityPlayer, container: ContainerCamping) extends InventoryPlayer(player, 4, 1, NBTInfo.INV_CAMPING) {
  val craftMatrix: InventoryCrafting =
    new InventoryCrafting(container, 3, 3)

  val craftMatrixSmall: InventoryCrafting =
    new InventoryCrafting(container, 2, 2)

  val craftResult: InventoryCraftResult =
    new InventoryCraftResult()

  val craftResultSmall: InventoryCraftResult =
    new InventoryCraftResult()

  private var backpackInv: Option[InventoryItem] =
    None

  override def openInventory(player:EntityPlayer) {
    super.openInventory(player)

    onChange(SLOT_BACKPACK)
  }

  override def onChange(slotNum: Int) {
    if (slotNum == SLOT_BACKPACK)
      backpackChanged()

    if(!player.world.isRemote)
      Objs.inventoryChanged.trigger(player.asInstanceOf[EntityPlayerMP], this)

  }

  def backpackChanged(): Unit = {
    val pack = getStackInSlot(SLOT_BACKPACK)

    if (!pack.isEmpty) {
      backpackInv = Some(new InventoryItem(pack, 27, 64))
      container.backpackChanged(pack, backpackInv)

      backpackInv.get.openInventory(player)
    } else {
      backpackInv = None
      container.backpackChanged(pack, backpackInv)
    }
  }

  override def closeInventory(player: EntityPlayer): Unit = {
    backpackInv.foreach(_.closeInventory(player))

    for (i <- 0 until 9)
      player.dropItem(craftMatrix.removeStackFromSlot(i), false)

    for (i <- 0 until 4)
      player.dropItem(craftMatrixSmall.removeStackFromSlot(i), false)

    super.closeInventory(player)
  }

  def getBackpack: Option[InventoryItem] =
    backpackInv
}
