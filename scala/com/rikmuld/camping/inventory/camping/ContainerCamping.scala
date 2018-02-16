package com.rikmuld.camping.inventory.camping

import com.rikmuld.camping.Lib.TextureInfo
import com.rikmuld.camping.inventory._
import com.rikmuld.camping.inventory.camping.InventoryCamping._
import com.rikmuld.camping.objs.Registry
import com.rikmuld.corerm.gui.container.{ContainerSimple, ContainerTabbed}
import com.rikmuld.corerm.inventory.InventoryItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.inventory._
import net.minecraft.item.{Item, ItemArmor, ItemStack}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

//TODO backpack items disappear if backpack is unused
class ContainerCamping(player:EntityPlayer) extends ContainerSimple[InventoryCamping](player) with ContainerTabbed {
  var backpackSlots: Seq[SlotTabbedBackpack] =
    _

  var backpackSlot: SlotItem =
    _

  var craftMatrix: InventoryCrafting =
    _

  var craftMatrixSmall: InventoryCrafting =
    _

  var craftResult: InventoryCraftResult =
    _

  var craftResultSmall: InventoryCraftResult =
    _

  override def playerInvX: Int =
    30

  def getID: String =
    "camping_inventory"

  override def addInventorySlots(): Unit = {
    backpackSlot = new SlotItem(getIInventory, 0, 8, 35, Registry.backpack)

    addSlotToContainer(backpackSlot)
    addSlotToContainer(new SlotItem(getIInventory, 1, 8, 53, Registry.knife))
    addSlotToContainer(new SlotItem(getIInventory, 2, 196, 35, Item.getItemFromBlock(Registry.lantern)))
    addSlotToContainer(new SlotItem(getIInventory, 3, 196, 53, Items.FILLED_MAP))

    backpackSlots = for (row <- 0 until 3; collom <- 0 until 9)
        yield new SlotTabbedBackpack(collom + (row * 9), 30 + (collom * 18), 17 + (row * 18), 1)

    backpackSlots.foreach(slot => addSlotToContainer(slot))

    addSlotToContainer(new SlotTabbedCrafting(player, craftMatrix, craftResult, 0, 147, 35, 2))
    addSlotToContainer(new SlotTabbedCrafting(player, craftMatrixSmall, craftResultSmall, 0, 176, 28, 0))

    for (row <- 0 until 3; collom <- 0 until 3)
      addSlotToContainer(new SlotTabbed(craftMatrix, collom + (row * 3), 53 + (collom * 18), 17 + (row * 18), 2))

    for (row <- 0 until 2; collom <- 0 until 2)
      addSlotToContainer(new SlotTabbed(craftMatrixSmall, collom + (row * 2), 120 + (collom * 18), 18 + (row * 18), 0))

    for (i <- 0 until 4)
      this.addSlotToContainer(new SlotTabbedArmor(player.inventory, player, 36 + (3 - i), 30, 8 + i * 18, 0, i))

    addSlotToContainer(new SlotTabbed(player.inventory, 40, 99, 62, 0) {
      @SideOnly(Side.CLIENT)
      override def getSlotTexture = TextureInfo.MC_INVENTORY_SHIELD
    })

    onCraftMatrixChanged(null)
  }

  override def initIInventory: InventoryCamping = {
    craftMatrix = new InventoryCrafting(this, 3, 3)
    craftMatrixSmall = new InventoryCrafting(this, 2, 2)
    craftResult = new InventoryCraftResult()
    craftResultSmall = new InventoryCraftResult()

    new InventoryCamping(player, Some(this))
  }

  override def onCraftMatrixChanged(inv: IInventory) {
    slotChangedCraftingGrid(player.world, player, craftMatrix, craftResult, 31)
    slotChangedCraftingGrid(player.world, player, craftMatrixSmall, craftResultSmall, 32)
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
      case item if item == Registry.backpack => mergeItemStack(stack, 0, 1, false)
      case item if item == Registry.knife => mergeItemStack(stack, 1, 2, false)
      case item if item == Item.getItemFromBlock(Registry.lantern) => mergeItemStack(stack, 2, 3, false)
      case item if item == Items.FILLED_MAP => mergeItemStack(stack, 3, 4, false)
      case _ => false
    }

    if(!success && getTab == GuiCamping.TAB_ARMOR && stack.getItem.isInstanceOf[ItemArmor]){
      success = mergeItemStack(stack, 46, 50, false)
    } else if(!success && index >= inventorySlots.size - 36 && getTab == GuiCamping.TAB_BACKPACK) {
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

  override def updateTab(player: EntityPlayer, tab: Int): Unit = {
    super.updateTab(player, tab)

    val backpackInv = getIInventory.getBackpack
    backpackChanged(backpackInv.fold(ItemStack.EMPTY)(_.getStack), getIInventory.getBackpack)
  }

  override def canMergeSlot(stack: ItemStack, slotIn: Slot): Boolean =
    (slotIn.inventory != craftResult) &&
      (slotIn.inventory != craftResultSmall) &&
      super.canMergeSlot(stack, slotIn)

  override def onContainerClosed(player: EntityPlayer): Unit = {
    super.onContainerClosed(player)

    for (i <- 0 until 9)
      player.dropItem(craftMatrix.removeStackFromSlot(i), false)

    for (i <- 0 until 4)
      player.dropItem(craftMatrixSmall.removeStackFromSlot(i), false)

  }
}