package com.rikmuld.camping.objs.registers

import net.minecraftforge.fml.relauncher.SideOnly
import com.rikmuld.corerm.misc.ModRegister
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.inventory.objs.ContainerTentChests
import com.rikmuld.camping.inventory.objs.GuiCampfireCook
import com.rikmuld.camping.inventory.objs.ContainerCampfireCook
import com.rikmuld.camping.ConfigGUI
import com.rikmuld.corerm.RMMod
import com.rikmuld.camping.inventory.objs.RucksackGui
import com.rikmuld.camping.inventory.objs.BackpackGui
import com.rikmuld.camping.inventory.objs.PouchGui
import com.rikmuld.camping.inventory.objs.GuiTrap
import com.rikmuld.camping.inventory.gui.GuiCampinginv
import com.rikmuld.camping.inventory.objs.GuiTentLanterns
import com.rikmuld.camping.inventory.objs.GuiTent
import com.rikmuld.camping.inventory.objs.GuiTentChests
import com.rikmuld.camping.inventory.objs.ContainerTrap
import com.rikmuld.camping.inventory.objs.ContainerTentLanterns
import com.rikmuld.camping.inventory.container.ContainerCampinv
import com.rikmuld.camping.inventory.objs.BackpackContainer
import com.rikmuld.camping.inventory.objs.RucksackContainer
import com.rikmuld.camping.inventory.objs.PouchContainer
import com.rikmuld.camping.inventory.objs.KitGui
import com.rikmuld.camping.inventory.objs.KitContainer
import com.rikmuld.camping.inventory.objs.GuiTentSleeping
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraft.util.SoundEvent
import com.rikmuld.camping.CampingMod
import net.minecraft.util.ResourceLocation

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
