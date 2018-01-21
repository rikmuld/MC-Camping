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
import com.rikmuld.camping.inventory.objs.ContainerEmpty

object ModGuis extends ModRegister{
  override def registerServer {
    guiPouch.fill(RMMod.proxy.registerGui(classOf[PouchContainer], null))
    guiBackpack.fill(RMMod.proxy.registerGui(classOf[BackpackContainer], null))
    guiRucksack.fill(RMMod.proxy.registerGui(classOf[RucksackContainer], null))
    guiKit.fill(RMMod.proxy.registerGui(classOf[KitContainer], null))
    guiCamping.fill(RMMod.proxy.registerGui(classOf[ContainerCampinv], null))
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
    guiCamping.fill(RMMod.proxy.registerGui(classOf[ContainerCampinv], classOf[GuiCampinginv]))
    guiTrap.fill(RMMod.proxy.registerGui(classOf[ContainerTrap], classOf[GuiTrap]))
    guiCampfireCook.fill(RMMod.proxy.registerGui(classOf[ContainerCampfireCook], classOf[GuiCampfireCook]))
    guiTent.fill(RMMod.proxy.registerGui(classOf[ContainerEmpty], classOf[GuiTent]))
    guiTentSleep.fill(RMMod.proxy.registerGui(null, classOf[GuiTentSleeping]))
    guiTentChests.fill(RMMod.proxy.registerGui(classOf[ContainerTentChests], classOf[GuiTentChests]))
    guiTentLantern.fill(RMMod.proxy.registerGui(classOf[ContainerTentLanterns], classOf[GuiTentLanterns]))
    guiConfig.fill(RMMod.proxy.registerGui(null, classOf[ConfigGUI]))
  }
}