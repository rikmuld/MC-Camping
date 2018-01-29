package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.ConfigGUI
import com.rikmuld.camping.Lib.Guis._
import com.rikmuld.camping.inventory.camping.{ContainerCamping, GuiCamping}
import com.rikmuld.camping.inventory.objs._
import com.rikmuld.corerm.Registry
import com.rikmuld.corerm.gui.{ContainerWrapper, ScreenWrapper}
import net.minecraftforge.fml.relauncher.Side

object ModGuis {
  def register(side: Side): Unit = {
    registerScreens(side)
    registerContainers()
  }

  def registerScreens(side: Side): Unit =
    Registry.screenRegistry.registerAll(
      ScreenWrapper.create(side, classOf[PouchGui], POUCH),
      ScreenWrapper.create(side, classOf[BackpackGui], BACKPACK),
      ScreenWrapper.create(side, classOf[RucksackGui], RUCKSACK),
      ScreenWrapper.create(side, classOf[GuiCampfireCook], CAMPFIRE_COOK),
      ScreenWrapper.create(side, classOf[GuiTent], TENT),
      ScreenWrapper.create(side, classOf[GuiTentChests], TENT_CHESTS),
      ScreenWrapper.create(side, classOf[GuiTentLanterns], TENT_LANTERNS),
      ScreenWrapper.create(side, classOf[GuiTentSleeping], TENT_SLEEP),
      ScreenWrapper.create(side, classOf[GuiCamping], CAMPING),
      ScreenWrapper.create(side, classOf[KitGui], KIT),
      ScreenWrapper.create(side, classOf[GuiTrap], TRAP),
      ScreenWrapper.create(side, classOf[ConfigGUI], CONFIG)
    )

  def registerContainers(): Unit =
    Registry.containerRegistry.registerAll(
      ContainerWrapper.create(classOf[PouchContainer], POUCH),
      ContainerWrapper.create(classOf[BackpackContainer], BACKPACK),
      ContainerWrapper.create(classOf[RucksackContainer], RUCKSACK),
      ContainerWrapper.create(classOf[ContainerCampfireCook], CAMPFIRE_COOK),
      ContainerWrapper.create(classOf[ContainerTentChests], TENT_CHESTS),
      ContainerWrapper.create(classOf[ContainerTentLanterns], TENT_LANTERNS),
      ContainerWrapper.create(classOf[ContainerCamping], CAMPING),
      ContainerWrapper.create(classOf[KitContainer], KIT),
      ContainerWrapper.create(classOf[ContainerTrap], TRAP)
    )
}