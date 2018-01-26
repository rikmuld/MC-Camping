package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.Lib.SoundInfo._
import com.rikmuld.camping.objs.Objs._
import net.minecraft.util.{ResourceLocation, SoundEvent}
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.registry.GameRegistry

object ModSounds {
  def register(registry: RegistryEvent.Register[SoundEvent]) {
    foxAmb = registerSound(FOX_SAY)
    foxDeath = registerSound(FOX_HURT)
    bearAmb = registerSound(BEAR_SAY)
    bearDeath = registerSound(BEAR_HURT)

    registry.getRegistry.registerAll(foxAmb, foxDeath, bearAmb, bearDeath)
  }
  
  def registerSound(location: ResourceLocation):SoundEvent =
    new SoundEvent(location).setRegistryName(location)
}
