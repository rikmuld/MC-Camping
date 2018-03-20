package com.rikmuld.camping.features.blocks.campfire.cook

import com.rikmuld.camping.Definitions.Kit
import com.rikmuld.camping.common.inventory.{SlotItem, SlotItemMeta}
import com.rikmuld.camping.features.blocks.campfire.cook.equipment.CookingEquipment
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.gui.container.ContainerSimple
import com.rikmuld.corerm.utils.WorldUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

class ContainerCampfireCook(player: EntityPlayer, tile: IInventory) extends ContainerSimple[TileEntityCampfireCook](player) {
  private var slots: Seq[SlotCooking] =
    _

  override def playerInvY: Int =
    106

  override def addInventorySlots(): Unit = {
    val thisPlayer = player
    val instance = this

    addSlotToContainer(new SlotItem(tile, 0, 80, 84, Items.COAL))
    addSlotToContainer(new SlotItemMeta(tile, 1, 150, 9, ObjRegistry.kit, Vector(Kit.SPIT, Kit.GRILL, Kit.PAN)) with SlotEquipment {
      val player: EntityPlayer = thisPlayer
      val container: ContainerCampfireCook = instance
    })

    slots = for (i <- 0 until 10)
      yield new SlotCooking(tile, i + 2, 0, 0)

    slots.foreach(slot => addSlotToContainer(slot))

    setSlots()
  }

  override def initIInventory: TileEntityCampfireCook =
    tile.asInstanceOf[TileEntityCampfireCook]

  override def mergeToInventory(stack: ItemStack, original: ItemStack, index: Int): Boolean =
    if (stack.getItem == Items.COAL) {
      mergeItemStack(stack, 0, 1, false)
    } else if (stack.getItem == ObjRegistry.kit && stack.getItemDamage != Kit.USELESS && stack.getItemDamage != Kit.EMPTY) {
      mergeItemStack(stack, 1, 2, false)
    } else if(getIInventory.getEquipment.fold(false)(_.canCook(stack))) {
      mergeItemStack(stack, 2, 2 + getIInventory.getEquipment.get.getMaxCookingSlot, false)
    } else false

  def getID: String =
    tile.getName

  def setSlots(): Unit =
    for (i <- 0 until 10)
      initializeSlot(i, slots(i), getIInventory.getEquipment)

  def initializeSlot(index: Int, slot: SlotCooking, equipment: Option[CookingEquipment]): Unit = {
    if (slot.active && !slot.getStack.isEmpty) {
      WorldUtils.dropItemInWorld(getIInventory.getWorld, slot.getStack, getIInventory.getPos)
      getIInventory.setInventorySlotContents(index + 2, ItemStack.EMPTY)
    }

    if (equipment.isEmpty)
      slot.deActivate()

    else if (index < equipment.get.getMaxCookingSlot) {
      val position = equipment.get.getSlotPosition(index)
      slot.activate(position._1, position._2, equipment.get, getIInventory)
    }
  }
}