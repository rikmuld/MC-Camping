package com.rikmuld.camping.features.items.knife

import com.rikmuld.camping.CampingMod
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent

@Mod.EventBusSubscriber
object EventsServer {

  @SubscribeEvent
  def onItemCrafted(event: ItemCraftedEvent): Unit =
    for (slot <- 0 until event.craftMatrix.getSizeInventory)
      event.craftMatrix.getStackInSlot(slot) match {

        case stack if stack.getItem == CampingMod.OBJ.knife =>
          stack.setCount(stack.getCount + 1)
          stack.damageItem(1, event.player)

        case _ =>
      }
}