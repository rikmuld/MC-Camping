package com.rikmuld.camping.objs

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.Definitions._
import com.rikmuld.camping.registers.ModMisc
import com.rikmuld.corerm.objs.blocks.BlockSimple
import com.rikmuld.corerm.objs.items.ItemSimple
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

//call post item and pre block registry

@EventBusSubscriber
object Registry {
  var knife,
      parts,
      marshmallow,
      backpack,
      kit,
      animalParts,
      venisonRaw,
      venisonCooked,
      furBoot,
      furChest,
      furHead,
      furLeg,
      campfireWoodItem,
      campfireCookItem,
      lanternItem,
      logSeatItem,
      trapItem,
      tentItem,
      sleepingBagItem,
      hempItem: ItemSimple =
    _

  var hemp,
      campfireWood,
      campfireCook,
      lantern,
      tent,
      logSeat,
      light,
      sleepingBag,
      trap,
      tentBounds: BlockSimple = _

  @SubscribeEvent
  def registerBlocks(event: RegistryEvent.Register[Block]): Unit = {
//    (hemp, hempItem) = HEMP.create(MOD_ID)
//    (campfireWood, campfireWoodItem) = CAMPFIRE_WOOD.create(MOD_ID)
//    (campfireCook, campfireCookItem) = CAMPFIRE_COOK.create(MOD_ID)
    val objLantern = LANTERN.create(MOD_ID)
    lantern = objLantern._1
    lanternItem = objLantern._2
//    (tent, tentItem) = TENT.create(MOD_ID)
//    (logSeat, logSeatItem) = LOGSEAT.create(MOD_ID)
//    light = LIGHT.createBlock(MOD_ID)
//    (sleepingBag, sleepingBagItem) = SLEEPING_BAG.create(MOD_ID)
    val objTrap = TRAP.create(MOD_ID)
    trap = objTrap._1
    trapItem = objTrap._2

//    tentBounds = BOUNDS_TENT.createBlock(MOD_ID)

    event.getRegistry.registerAll(
//      hemp,
//      campfireWood,
//      campfireCook,
      lantern,
//      tent,
//      logSeat,
//      light,
//      sleepingBag,
      trap
//      tentBounds
    )
  }

  @SubscribeEvent
  def registerItems(event: RegistryEvent.Register[Item]): Unit = {
    knife = KNIFE.createItem(MOD_ID)
    parts = PARTS.createItem(MOD_ID)
    marshmallow = MARSHMALLOW.createItem(MOD_ID)
    backpack = BACKPACK.createItem(MOD_ID)
    kit = KIT.createItem(MOD_ID)
    animalParts = PARTS_ANIMAL.createItem(MOD_ID)
    venisonRaw = VENISON_RAW.createItem(MOD_ID)
    venisonCooked = VENISON_COOKED.createItem(MOD_ID)
    furBoot = FUR_BOOT.createItem(MOD_ID)
    furChest = FUR_CHEST.createItem(MOD_ID)
    furHead = FUR_HEAD.createItem(MOD_ID)
    furLeg = FUR_LEG.createItem(MOD_ID)

    ModMisc.registerCookingEquipment()
    event.getRegistry.registerAll(
      knife,
      parts,
      marshmallow,
      backpack,
      kit,
      animalParts,
      venisonRaw,
      venisonCooked,
      furBoot,
      furChest,
      furHead,
      furLeg,
//      campfireWoodItem,
//      campfireCookItem,
      lanternItem,
//      logSeatItem,
      trapItem
//      tentItem,
//      sleepingBagItem,
//      hempItem
    )
  }

  @SubscribeEvent
  def registerItemRenders(event: ModelRegistryEvent): Unit = {
    knife.registerRenders()
    parts.registerRenders()
    marshmallow.registerRenders()
    backpack.registerRenders()
    kit.registerRenders()
    animalParts.registerRenders()
    venisonRaw.registerRenders()
    venisonCooked.registerRenders()
    furBoot.registerRenders()
    furChest.registerRenders()
    furHead.registerRenders()
    furLeg.registerRenders()
//    campfireWoodItem.registerRenders()
//    campfireCookItem.registerRenders()
    lanternItem.registerRenders()
//    logSeatItem.registerRenders()
    trapItem.registerRenders()
//    tentItem.registerRenders()
//    sleepingBagItem.registerRenders()
//    hempItem.registerRenders()
  }
}