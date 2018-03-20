package com.rikmuld.camping.features.items.bags

import com.rikmuld.camping.registers.ObjRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent

@Mod.EventBusSubscriber
object EventsServer {

  @SubscribeEvent
  def onItemCrafted(event: ItemCraftedEvent): Unit =
    for(slot <- 0 until event.craftMatrix.getSizeInventory)
      event.craftMatrix.getStackInSlot(slot) match {

        case stack if stack.getItem == ObjRegistry.backpack =>
          event.crafting.setTagCompound(stack.getTagCompound)

        case _ =>
      }
}
