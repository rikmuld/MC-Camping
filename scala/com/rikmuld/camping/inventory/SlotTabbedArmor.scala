package com.rikmuld.camping.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot._
import net.minecraft.inventory.IInventory
import net.minecraft.item.{ItemArmor, ItemStack}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object SlotTabbedArmor {
  val armorSlots = List(HEAD, CHEST, LEGS, FEET)
}

class SlotTabbedArmor(inv: IInventory, player: EntityPlayer, index: Int, x:Int, y:Int, tab: Int, armorIndex: Int)
  extends SlotTabbed(inv, index, x, y, tab) {

  override def getSlotStackLimit =
    1

  override def isItemValid(stack: ItemStack): Boolean =
    stack.getItem.isValidArmor(stack, SlotTabbedArmor.armorSlots(armorIndex), player)

  @SideOnly(Side.CLIENT)
  override def getSlotTexture = ItemArmor.EMPTY_SLOT_NAMES(3 - armorIndex)
}