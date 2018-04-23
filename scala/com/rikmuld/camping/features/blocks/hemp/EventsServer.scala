package com.rikmuld.camping.features.blocks.hemp

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.Hemp
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.{Event, SubscribeEvent}

@Mod.EventBusSubscriber
object EventsServer {

  @SubscribeEvent
  def onBoneMealUsed(event: BonemealEvent): Unit =
    if (event.getBlock.getBlock == CampingMod.OBJ.hemp) {
      val world = event.getWorld
      val pos = event.getPos
      val hemp = CampingMod.OBJ.hemp.asInstanceOf[BlockHemp]

      val result =
        if (hemp.getInt(world, pos, Hemp.STATE_AGE) <= Hemp.STATE_AGE_READY) {
          hemp.grow(world, pos)
          Event.Result.ALLOW
        } else
          Event.Result.DENY

      event.setResult(result)
    }
}
