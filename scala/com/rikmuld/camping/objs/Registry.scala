package com.rikmuld.camping.objs

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.misc._
import com.rikmuld.camping.objs.Definitions._
import com.rikmuld.camping.registers.Objs._
import com.rikmuld.corerm.objs.blocks.BlockSimple
import com.rikmuld.corerm.objs.items.ItemSimple
import net.minecraft.block.Block
import net.minecraft.init.Blocks.IRON_BARS
import net.minecraft.init.Items._
import net.minecraft.init.SoundEvents
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.item.{Item, ItemFood, ItemStack}
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry

import scala.collection.JavaConversions._

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
      campfireWoodOn,
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
    preRegistry()

    val objHemp = HEMP.create(MOD_ID)
    hemp = objHemp._1
    hempItem = objHemp._2

    val objCampfireWood = CAMPFIRE_WOOD_OFF.create(MOD_ID)
    campfireWood = objCampfireWood._1
    campfireWoodItem = objCampfireWood._2

    campfireWoodOn = CAMPFIRE_WOOD_ON.createBlock(MOD_ID)

    val objCampfireCook = CAMPFIRE_COOK.create(MOD_ID)
    campfireCook = objCampfireCook._1
    campfireCookItem = objCampfireCook._2

    val objLantern = LANTERN.create(MOD_ID)
    lantern = objLantern._1
    lanternItem = objLantern._2

    val objTent = TENT.create(MOD_ID)
    tent = objTent._1
    tentItem = objTent._2

    val logSeatObj = LOGSEAT.create(MOD_ID)
    logSeat = logSeatObj._1
    logSeatItem = logSeatObj._2

    light = LIGHT.createBlock(MOD_ID)

    val sleepingBagObj = SLEEPING_BAG.create(MOD_ID)
    sleepingBag = sleepingBagObj._1
    sleepingBagItem = sleepingBagObj._2

    val objTrap = TRAP.create(MOD_ID)
    trap = objTrap._1
    trapItem = objTrap._2

//    tentBounds = BOUNDS_TENT.createBlock(MOD_ID)

    event.getRegistry.registerAll(
      hemp,
      campfireWood,
      campfireWoodOn,
      campfireCook,
      lantern,
      tent,
      logSeat,
      light,
      sleepingBag,
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
      campfireWoodItem,
      campfireCookItem,
      lanternItem,
      logSeatItem,
      trapItem,
      tentItem,
      sleepingBagItem,
      hempItem
    )

    postRegistry()
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
    campfireWoodItem.registerRenders()
    campfireCookItem.registerRenders()
    lanternItem.registerRenders()
    logSeatItem.registerRenders()
    trapItem.registerRenders()
    tentItem.registerRenders()
    sleepingBagItem.registerRenders()
    hempItem.registerRenders()
  }

  def preRegistry(): Unit = {
    tab = new TabCamping(MOD_ID)
    fur = EnumHelper.addArmorMaterial("FUR", "", 20, Array(2, 5, 4, 2), 20, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0)
  }

  def postRegistry(): Unit = {
    GameRegistry.addSmelting(new ItemStack(venisonRaw), new ItemStack(venisonCooked), 3)

    grill = new Grill()
    spit = new Spit()
    pan = new Pan()

    CookingEquipment.registerKitRecipe(spit,
      new ItemStack(STICK, 2),
      new ItemStack(Registry.parts, 1, Parts.STICK_IRON)
    )

    CookingEquipment.registerKitRecipe(grill,
      new ItemStack(STICK, 4),
      new ItemStack(Registry.parts, 2, Parts.STICK_IRON),
      new ItemStack(IRON_BARS)
    )

    CookingEquipment.registerKitRecipe(pan,
      new ItemStack(STICK, 3),
      new ItemStack(Registry.parts, 1, Parts.STICK_IRON),
      new ItemStack(Registry.parts, 1, Parts.PAN)
    )

    grill.registerRecipe(FISH, 0, new ItemStack(COOKED_FISH, 1, 0))
    grill.registerRecipe(FISH, 1, new ItemStack(COOKED_FISH, 1, 1))
    grill.registerRecipe(BEEF, new ItemStack(COOKED_BEEF))
    grill.registerRecipe(PORKCHOP, new ItemStack(COOKED_PORKCHOP))
    grill.registerRecipe(venisonRaw, new ItemStack(venisonCooked))
    grill.registerRecipe(MUTTON, new ItemStack(COOKED_MUTTON))

    pan.registerRecipe(POTATO, new ItemStack(BAKED_POTATO))
    pan.registerRecipe(ROTTEN_FLESH, new ItemStack(LEATHER))

    spit.registerRecipe(CHICKEN, new ItemStack(COOKED_CHICKEN))
    spit.registerRecipe(RABBIT, new ItemStack(COOKED_RABBIT))
    spit.registerRecipe(FISH, 0, new ItemStack(COOKED_FISH, 1, 0))
    spit.registerRecipe(FISH, 1, new ItemStack(COOKED_FISH, 1, 1))

    FurnaceRecipes.instance.getSmeltingList.foreach(stack =>
      stack._1.getItem match {
        case food: ItemFood =>
          if(food.isWolfsFavoriteMeat)
            grill.registerRecipe(food, stack._1.getItemDamage, stack._2)
          else
            pan.registerRecipe(food, stack._1.getItemDamage, stack._2)
        case _ =>
      }
    )
  }
}