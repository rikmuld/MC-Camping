package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.corerm.misc.ModRegister
import net.minecraft.util.{ResourceLocation, SoundEvent}
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object ModSounds extends ModRegister{
  @SideOnly(Side.CLIENT)
  override def registerClient {
    foxAmb = registerSound("mob.fox.say")
    foxDeath = registerSound("mob.fox.dead")
    bearAmb = registerSound("mob.bear.say")
    bearDeath = registerSound("mob.bear.dead")
  }
  
  def registerSound(src:String):SoundEvent = {
    val sound = new ResourceLocation(CampingMod.MOD_ID, src);
    GameRegistry.register(new SoundEvent(sound).setRegistryName(sound));
  }
}
