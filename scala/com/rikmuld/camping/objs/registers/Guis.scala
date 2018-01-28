package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.ConfigGUI
import com.rikmuld.camping.inventory.camping.{ContainerCamping, GuiCamping}
import com.rikmuld.camping.inventory.objs._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.corerm.RMMod
import com.rikmuld.corerm.utils.ModRegister
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object ModGuis extends ModRegister{
  override def registerServer {
    guiPouch.fill(RMMod.proxy.registerGui(classOf[PouchContainer], null))
    guiBackpack.fill(RMMod.proxy.registerGui(classOf[BackpackContainer], null))
    guiRucksack.fill(RMMod.proxy.registerGui(classOf[RucksackContainer], null))
    guiKit.fill(RMMod.proxy.registerGui(classOf[KitContainer], null))
    guiCamping.fill(RMMod.proxy.registerGui(classOf[ContainerCamping], null))
    guiTrap.fill(RMMod.proxy.registerGui(classOf[ContainerTrap], null))
    guiCampfireCook.fill(RMMod.proxy.registerGui(classOf[ContainerCampfireCook], null))
    guiTent.fill(RMMod.proxy.registerGui(classOf[ContainerEmpty], null))
    guiTentSleep.fill(RMMod.proxy.registerGui(null, null))
    guiTentChests.fill(RMMod.proxy.registerGui(classOf[ContainerTentChests], null))
    guiTentLantern.fill(RMMod.proxy.registerGui(classOf[ContainerTentLanterns], null))
    guiConfig.fill(RMMod.proxy.registerGui(null, null))
  }
  
  @SideOnly(Side.CLIENT)
  override def registerClient {
    guiPouch.fill(RMMod.proxy.registerGui(classOf[PouchContainer], classOf[PouchGui]))
    guiBackpack.fill(RMMod.proxy.registerGui(classOf[BackpackContainer], classOf[BackpackGui]))
    guiRucksack.fill(RMMod.proxy.registerGui(classOf[RucksackContainer], classOf[RucksackGui]))
    guiKit.fill(RMMod.proxy.registerGui(classOf[KitContainer], classOf[KitGui]))
    guiCamping.fill(RMMod.proxy.registerGui(classOf[ContainerCamping], classOf[GuiCamping]))
    guiTrap.fill(RMMod.proxy.registerGui(classOf[ContainerTrap], classOf[GuiTrap]))
    guiCampfireCook.fill(RMMod.proxy.registerGui(classOf[ContainerCampfireCook], classOf[GuiCampfireCook]))
    guiTent.fill(RMMod.proxy.registerGui(classOf[ContainerEmpty], classOf[GuiTent]))
    guiTentSleep.fill(RMMod.proxy.registerGui(null, classOf[GuiTentSleeping]))
    guiTentChests.fill(RMMod.proxy.registerGui(classOf[ContainerTentChests], classOf[GuiTentChests]))
    guiTentLantern.fill(RMMod.proxy.registerGui(classOf[ContainerTentLanterns], classOf[GuiTentLanterns]))
    guiConfig.fill(RMMod.proxy.registerGui(null, classOf[ConfigGUI]))
  }
}