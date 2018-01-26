package com.rikmuld.camping

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.registers._
import com.rikmuld.corerm.misc.ModRegister
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.{ResourceLocation, SoundEvent}
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.{Mod, SidedProxy}
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import org.lwjgl.input.Keyboard

import scala.collection.mutable.ListBuffer

@Mod.EventBusSubscriber@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES, modLanguage = MOD_LANUAGE, guiFactory = MOD_GUIFACTORY)
object CampingMod {
  final val MOD_ID = "camping"
  final val MOD_NAME = "The Camping Mod 2"
  final val MOD_VERSION = "2.3f"
  final val MOD_LANUAGE = "scala"
  final val MOD_DEPENDENCIES = "required-after:forge@[v13.20.1.2386,);required-after:corerm@[1.2g,)"
  final val MOD_SERVER_PROXY = "com.rikmuld."+MOD_ID+".ProxyServer"
  final val MOD_CLIENT_PROXY = "com.rikmuld."+MOD_ID+".ProxyClient"
  final val MOD_GUIFACTORY = "com.rikmuld.camping.GuiFactory"
  final val PACKET_CHANEL = MOD_ID
  
  val registers = new ListBuffer[ModRegister]

  @SidedProxy(clientSide = MOD_CLIENT_PROXY, serverSide = MOD_SERVER_PROXY)
  var proxy: ProxyServer = _
  var config: Config = _

  @EventHandler
  def preInit(event: FMLPreInitializationEvent) {
    config = new Config(new Configuration(event.getSuggestedConfigurationFile()))
    config.sync
    proxy.registerEvents

    register(event.getSide, ModEntities, ModRegister.PRE)
  }
  @EventHandler
  def Init(event: FMLInitializationEvent) {
    if(event.getSide == Side.CLIENT) ModBlocks.registerClient()

    register(event.getSide, ModMisc, ModRegister.PERI)
    register(event.getSide, ModGuis, ModRegister.PERI)
    register(event.getSide, ModEntities, ModRegister.PERI)
    register(event.getSide, ModTiles, ModRegister.PERI)

    ModRecipes.register()
  }
  @EventHandler
  def PosInit(event: FMLPostInitializationEvent) {
    register(event.getSide, ModEntities, ModRegister.POST)
    register(event.getSide, ModMisc, ModRegister.POST)
  }

  def register(side:Side, register:ModRegister, phase:Int) {    
    register.phase=phase

    register.register
    if(side == Side.CLIENT)register.registerClient
    if(side == Side.SERVER)register.registerServer    
  }

  @SubscribeEvent
  def registerBlock(event: RegistryEvent.Register[Block]): Unit = {
    ModBlocks.preInit()
    ModBlocks.createBlocks()
    ModBlocks.registerBlocks(event)
  }

  @SubscribeEvent
  def registerItem(event: RegistryEvent.Register[Item]): Unit ={
    ModItems.createItems()
    ModItems.registerItems(event)

    ModBlocks.registerItemBlocks(event)
  }

  @SubscribeEvent
  def registerModels(event: ModelRegistryEvent): Unit ={
    ModItems.registerModels(event)
    ModBlocks.registerModels(event)
  }

  @SubscribeEvent
  def registerSounds(event: RegistryEvent.Register[SoundEvent]): Unit ={
    ModSounds.register(event)
  }
}

object Lib {
  object ConfigInfo {
    final val CAT_ANIMALS = "animals"
    final val CAT_WORLD = "world genaration"
    final val CAT_TOOLS = "tools"
    final val CAT_FOOD = "food"
    final val CAT_GENERAL = "general"
    final val CAT_CAMPFIRE = "campfire"
  }
  
  object EntityInfo {
    final val BEAR = 0
    final val FOX = 1
    final val CAMPER = 2
    final val MOUNTABLE = 3
  }
  
  object AchievementInfo {
    final val KNIFE_GET = "knifeGet"
    final val FULL_CAMPER = "fullCamper"
    final val EXPLORER = "explorer"
    final val WILD_MAN = "wildMan"
    final val TENT_SLEEP = "backTBasic"
    final val LUXURY_TENT = "luxury"
    final val MARSHMELLOW = "roasting"
    final val MAD_CAMPER = "madCamper"
    final val CAMPFIRE_MASTERY = "campfire"
    final val HUNTER = "hunter"
    final val PROTECTOR = "protector"
  }
  
  object NBTInfo {
    final val INV_CAMPING = "campInv"
    final val ACHIEVEMENTS = "camping_achieve"
  }

  object SoundInfo {
    final val BEAR_SAY = new ResourceLocation(MOD_ID, "mob.bear.say")
    final val BEAR_HURT = new ResourceLocation(MOD_ID, "mob.bear.hurt")
    final val FOX_SAY = new ResourceLocation(MOD_ID, "mob.fox.say")
    final val FOX_HURT = new ResourceLocation(MOD_ID, "mob.fox.hurt")
  }
  
  object PotionInfo {
    final val BLEEDING = "Bleeding"
  }
  
  object DamageInfo {
    final val BLEEDING = "bleedingSource"
  }

  object KeyInfo {
    final val CATAGORY_MOD = "The Camping Mod"
    final val INVENTORY_KEY = 0
  
    final val desc: Array[String] = Array("Camping Inventory Key")
  
    final val default: Array[Int] = Array(Keyboard.KEY_G)
  }
  
  object TextureInfo {
    final val GUI_LOCATION = MOD_ID + ":textures/gui/"
    final val MODEL_LOCATION = MOD_ID + ":textures/models/"
    final val SPRITE_LOCATION = MOD_ID + ":textures/sprite/"
    final val MC_LOCATION = "minecraft:textures/"
    final val MC_ITEM_LOCATION = "minecraft:items/"
    final val MC_GUI_LOCATION = MC_LOCATION + "gui/"
  
    final val SPRITE_FX = SPRITE_LOCATION + "fx.png"
    final val SPRITE_POTION = SPRITE_LOCATION + "potion.png"

    final val MC_INVENTORY = MC_GUI_LOCATION + "inventory.png"
    final val MC_INVENTORY_SHIELD = MC_ITEM_LOCATION + "empty_armor_slot_shield"

    final val GUI_CAMPINV_BACK = GUI_LOCATION + "camping_backpack.png"
    final val GUI_CAMPINV_TOOL = GUI_LOCATION + "camping_tool.png"
    final val GUI_BAG = GUI_LOCATION + "backpack.png"
    final val GUI_KIT = GUI_LOCATION + "kit.png"
    final val GUI_CAMPINV = GUI_LOCATION + "campinv.png"
    final val GUI_CAMPINV_CRAFT = GUI_LOCATION + "campinv_craft.png"
    final val GUI_CAMPFIRE_COOK = GUI_LOCATION + "campfire_cook.png"
    final val GUI_UTILS = GUI_LOCATION + "utils.png"
    final val GUI_TRAP = GUI_LOCATION + "trap.png"
    final val RED_DOT = GUI_LOCATION + "map_red.png"
    final val BLUE_DOT = GUI_LOCATION + "map_blue.png"
    final val GUI_TENT = GUI_LOCATION + "tent.png"
    final val GUI_TENT_CONTENDS_1 = GUI_LOCATION + "tent_contend1.png"
    final val GUI_TENT_CONTENDS_2 = GUI_LOCATION + "tent_contend2.png"

    final val MODEL_CAMPFIRE = MODEL_LOCATION + "campfire_deco.png"
    final val MODEL_GRILL = MODEL_LOCATION + "grill.png"
    final val MODEL_PAN = MODEL_LOCATION + "pan.png"
    final val MODEL_LOG = MODEL_LOCATION + "log.png"
    final val MODEL_LOG2 = MODEL_LOCATION + "log2.png"
    final val MODEL_SPIT = MODEL_LOCATION + "spit.png"
    final val MODEL_SLEEPING_TOP = MODEL_LOCATION + "sleeping_bag_top.png"
    final val MODEL_SLEEPING_DOWN = MODEL_LOCATION + "sleeping_bag_down.png"
    final val MODEL_TENT_WHITE = MODEL_LOCATION + "tent_white.png"
    final val MODEL_BEAR = MODEL_LOCATION + "bear.png"
    final val MODEL_FOX = MODEL_LOCATION + "arctic_fox.png"
    final val MODEL_TRAP = MODEL_LOCATION + "bear_trap_open.png"
    final val MODEL_CAMPER_FEMALE = MODEL_LOCATION + "camper_female.png"
    final val MODEL_CAMPER_MALE = MODEL_LOCATION + "camper_male.png"
    final val ARMOR_FUR_LEG = MODEL_LOCATION + "armor_fur_legs.png"
    final val ARMOR_FUR_MAIN = MODEL_LOCATION + "armor_fur_main.png"
  }
}

/*
plan: add advancements, test server and release, then cleanup gui client stuff (first all from tabbed, then camping inv and tent
      [improve tent gui to a tabbed one]) then release, then make recipes visible with advancements and add own recipes
      as json and put recipe book in camping inventory then release, finally add spawner object thingies for campfires and release last before bugfix update

  TODO for port to 1.12
  - add advancement json files
  - remove achievement localization
  - add custom advancement triggers

  TODO after for next update
  - make tent tabbed gui
  - add own recipes as json recipes
  - unlock all recipes before crafting using advancements
  - add recipe book to camping inventory
  - update website, all recipes including spit stuff in cookbook
  - 'spawner' blocks for special cooking fires, spit, grill, pan in creative tab

  TODO after for next update (Camping Mod 2.2, the bugfix update :P)
  - continue rewrite: guis, tiles, utils, block and items, network, corerm features, registry system, others
  - fix github bugs that remain after rewrite + extensive bugtest

  TODO after for next update (Camping Mod 3?, but release slowly as 3α_buildnr)
  - more food to roast (some ideas: sausage, bacon, egg on a stick, any meat/vegtable is stickable together for a nice ... what is it called again...)
  - stickable food on either wood or iron (wood burns iron stays after eating)
  - add pressure plate to bear trap
  - re-balance recipes, and while:
    - simple tent from wheat or leaves, just for sleeping
    - simple sleeping bag for just one night (breaks afterwards)
    - simple tool version with less durability
    - change kit system (no GUI, build the campfire types on a normal campfire (also make them a blockstate being in creative tab))
      then everything build can be put into a kit to quickly switch it arround
    - stickable food can be put inside campfire as the simpelest cooking fire (iron stays, wood burns (after eating))
  - improve world gen, use much more block, more variation
  - entities: smarter, fox: not only arctic, make polar bear drop fur as well
  - item ideas:
    - first aid kit (to heal and cure bleeding), also add bleeding effects when attaked by a bear, or arrows
    - ropes (for climbing on mountains)
  - improve tent system, but ugly now, also perhaps tens build a bit more interestingly in the world directly (scalable :P)
  - stuff arround items, blocks: advnacements make exciting journy like goals; specific tracks
 */