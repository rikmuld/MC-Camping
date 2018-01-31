package com.rikmuld.camping.registers

import com.rikmuld.camping.Lib.SoundInfo._
import Objs._
import net.minecraft.util.{ResourceLocation, SoundEvent}
import net.minecraftforge.event.RegistryEvent

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
