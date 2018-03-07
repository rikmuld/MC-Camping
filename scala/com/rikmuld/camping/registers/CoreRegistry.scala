package com.rikmuld.camping.registers

import com.rikmuld.camping.ConfigGUI
import com.rikmuld.camping.Library.GuiInfo._
import com.rikmuld.camping.advancements._
import com.rikmuld.camping.inventory.camping.{ContainerCamping, GuiCamping}
import com.rikmuld.camping.inventory.objs._
import com.rikmuld.camping.misc._
import com.rikmuld.corerm.advancements.TriggerRegistry
import com.rikmuld.corerm.gui.{ContainerWrapper, ScreenWrapper}
import com.rikmuld.corerm.network.PacketWrapper
import com.rikmuld.corerm.registry.RMRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.registries.ForgeRegistry

object CoreRegistry extends RMRegistry {
  override def registerScreens(registry: ForgeRegistry[ScreenWrapper], side: Side): Unit =
    registry.registerAll(
      ScreenWrapper.create(side, classOf[PouchGui], POUCH),
      ScreenWrapper.create(side, classOf[BackpackGui], BACKPACK),
      ScreenWrapper.create(side, classOf[RucksackGui], RUCKSACK),
      ScreenWrapper.create(side, classOf[KitGui], KIT),
      ScreenWrapper.create(side, classOf[GuiCampfireCook], CAMPFIRE_COOK),
      ScreenWrapper.create(side, classOf[GuiTent], TENT),
      ScreenWrapper.create(side, classOf[GuiTentChests], TENT_CHESTS),
      ScreenWrapper.create(side, classOf[GuiTentLanterns], TENT_LANTERNS),
      ScreenWrapper.create(side, classOf[GuiTentSleeping], TENT_SLEEP),
      ScreenWrapper.create(side, classOf[GuiCamping], CAMPING),
      ScreenWrapper.create(side, classOf[GuiTrap], TRAP),
      ScreenWrapper.create(side, classOf[ConfigGUI], CONFIG)
    )

  override def registerContainers(registry: ForgeRegistry[ContainerWrapper]): Unit =
    registry.registerAll(
      ContainerWrapper.create(classOf[PouchContainer], POUCH),
      ContainerWrapper.create(classOf[BackpackContainer], BACKPACK),
      ContainerWrapper.create(classOf[RucksackContainer], RUCKSACK),
      ContainerWrapper.create(classOf[KitContainer], KIT),
      ContainerWrapper.create(classOf[ContainerCampfireCook], CAMPFIRE_COOK),
      ContainerWrapper.create(classOf[ContainerTentChests], TENT_CHESTS),
      ContainerWrapper.create(classOf[ContainerTentLanterns], TENT_LANTERNS),
      ContainerWrapper.create(classOf[ContainerCamping], CAMPING),
      ContainerWrapper.create(classOf[ContainerTrap], TRAP)
    )

  override def registerPackets(registry: ForgeRegistry[PacketWrapper]): Unit =
    registry.registerAll(
      PacketWrapper.create(classOf[NBTPlayer], "nbtPlayer"),
      PacketWrapper.create(classOf[KeyData], "keyData"),
      PacketWrapper.create(classOf[ItemsData], "itemData"),
      PacketWrapper.create(classOf[PlayerExitLog], "playerExitLog"),
      PacketWrapper.create(classOf[PlayerSleepInTent], "playerSleepInTent"),
      PacketWrapper.create(classOf[MapData], "mapData")
    )

  override def registerTriggers(registry: TriggerRegistry): Unit =
    registry.registerAll(
      new Slept.Trigger,
      new CampfireMade.Trigger,
      new DyeBurned.Trigger,
      new EntityTrapped.Trigger,
      new FoodRoasted.Trigger,
      new InventoryChanged.Trigger,
      new TentChanged.Trigger,
      new CamperInteract.Trigger
    )
}