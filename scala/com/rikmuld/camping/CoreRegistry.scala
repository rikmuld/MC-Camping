package com.rikmuld.camping

import com.rikmuld.camping.Lib.GuiInfo._
import com.rikmuld.camping.advancements._
import com.rikmuld.camping.inventory.camping.{ContainerCamping, GuiCamping}
import com.rikmuld.camping.inventory.objs._
import com.rikmuld.camping.misc._
import com.rikmuld.corerm.gui.{ContainerWrapper, ScreenWrapper}
import com.rikmuld.corerm.network.PacketWrapper
import com.rikmuld.corerm.registry.RMRegistryEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@EventBusSubscriber
object CoreRegistry {

  @SubscribeEvent
  def registerScreens(event: RMRegistryEvent.Screens): Unit =
    event.getRegistry.registerAll(
      ScreenWrapper.create(event.getSide, classOf[PouchGui], POUCH),
      ScreenWrapper.create(event.getSide, classOf[BackpackGui], BACKPACK),
      ScreenWrapper.create(event.getSide, classOf[RucksackGui], RUCKSACK),
      ScreenWrapper.create(event.getSide, classOf[KitGui], KIT),
      ScreenWrapper.create(event.getSide, classOf[GuiCampfireCook], CAMPFIRE_COOK),
//      ScreenWrapper.create(event.getSide, classOf[GuiTent], TENT),
//      ScreenWrapper.create(event.getSide, classOf[GuiTentChests], TENT_CHESTS),
//      ScreenWrapper.create(event.getSide, classOf[GuiTentLanterns], TENT_LANTERNS),
//      ScreenWrapper.create(event.getSide, classOf[GuiTentSleeping], TENT_SLEEP),
      ScreenWrapper.create(event.getSide, classOf[GuiCamping], CAMPING),
      ScreenWrapper.create(event.getSide, classOf[GuiTrap], TRAP),
      ScreenWrapper.create(event.getSide, classOf[ConfigGUI], CONFIG)
    )

  @SubscribeEvent
  def registerContainers(event: RMRegistryEvent.Containers): Unit =
    event.getRegistry.registerAll(
      ContainerWrapper.create(classOf[PouchContainer], POUCH),
      ContainerWrapper.create(classOf[BackpackContainer], BACKPACK),
      ContainerWrapper.create(classOf[RucksackContainer], RUCKSACK),
      ContainerWrapper.create(classOf[KitContainer], KIT),
      ContainerWrapper.create(classOf[ContainerCampfireCook], CAMPFIRE_COOK),
//      ContainerWrapper.create(classOf[ContainerTentChests], TENT_CHESTS),
//      ContainerWrapper.create(classOf[ContainerTentLanterns], TENT_LANTERNS),
      ContainerWrapper.create(classOf[ContainerCamping], CAMPING),
      ContainerWrapper.create(classOf[ContainerTrap], TRAP)
    )

  @SubscribeEvent
  def registerPackets(event: RMRegistryEvent.Packets): Unit =
    event.getRegistry.registerAll(
      PacketWrapper.create(classOf[NBTPlayer], "nbtPlayer"),
      PacketWrapper.create(classOf[KeyData], "keyData"),
      PacketWrapper.create(classOf[ItemsData], "itemData"),
      PacketWrapper.create(classOf[PlayerExitLog], "playerExitLog"),
//      PacketWrapper.create(classOf[PlayerSleepInTent], "playerSleepInTent"),
      PacketWrapper.create(classOf[MapData], "mapData")
    )

  @SubscribeEvent
  def registerTriggers(event: RMRegistryEvent.Advancements): Unit =
    event.getRegistry.registerAll(
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