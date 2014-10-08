package com.rikmuld.camping.core

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.client.event.ConfigChangedEvent
import com.rikmuld.camping.CampingMod

class Events {
  @SubscribeEvent
  def onConfigChanged(eventArgs:ConfigChangedEvent.OnConfigChangedEvent) {
    if(eventArgs.modID.equals(ModInfo.MOD_ID))Objs.config.sync
  }
}