package com.rikmuld.camping.features.general.config

import com.rikmuld.camping.CampingMod._
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber
object EventsServer {

  @SubscribeEvent
  def onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent):Unit =
    if (eventArgs.getModID.equals(MOD_ID))
      CONFIG.sync()
}