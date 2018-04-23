package com.rikmuld.camping.features.items.parts

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.Parts
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent

@Mod.EventBusSubscriber
object EventsServer {

  final val MARSHMALLOWS =
    new ItemStack(CampingMod.OBJ.parts, 1, Parts.MARSHMALLOW)

  @SubscribeEvent
  def onItemCrafted(event: ItemCraftedEvent): Unit =
    if(event.crafting.isItemEqual(MARSHMALLOWS))
      for (slot <- 0 until event.craftMatrix.getSizeInventory)
        event.craftMatrix.getStackInSlot(slot) match {

          case stack if stack.getItem == Items.BOWL =>
            stack.setCount(stack.getCount + 1)

          case stack if stack.getItem == Items.POTIONITEM =>
            event.player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE, 1))

          case _ =>
        }
}