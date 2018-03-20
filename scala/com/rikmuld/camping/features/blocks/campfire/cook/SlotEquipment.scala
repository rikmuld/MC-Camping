package com.rikmuld.camping.features.blocks.campfire.cook

import com.rikmuld.camping.Library.AdvancementInfo
import com.rikmuld.corerm.advancements.TriggerHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot

trait SlotEquipment extends Slot {
  val player: EntityPlayer

  val container: ContainerCampfireCook

  override def onSlotChanged(): Unit = {
    super.onSlotChanged()

    container.setSlots()

    if (!getStack.isEmpty && !player.world.isRemote)
      TriggerHelper.trigger(AdvancementInfo.CAMPFIRES_MADE, player, getStack.getItemDamage)
  }
}