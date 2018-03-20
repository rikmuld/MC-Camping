package com.rikmuld.camping

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.registers._
import com.rikmuld.corerm.RMMod
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES, modLanguage = MOD_LANUAGE)
object CampingMod {
  final val MOD_ID = "camping"
  final val MOD_NAME = "The Camping Mod 2"
  final val MOD_VERSION = "2.4.0"
  final val MOD_LANUAGE = "scala"
  final val MOD_DEPENDENCIES = "required-after:forge@[v13.20.1.2386,);required-after:corerm@[1.3.0,)"
  final val PACKET_CHANEL = MOD_ID

  var config: Config = _

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    config = new Config(new Configuration(event.getSuggestedConfigurationFile))
    config.sync()

    MiscRegistry.register(event)
  }

  @EventHandler
  def Init(event: FMLInitializationEvent): Unit = {
    RMMod.register(event, CoreRegistry)
    MiscRegistry.register(event)
  }

  @EventHandler
  def PosInit(event: FMLPostInitializationEvent): Unit =
    MiscRegistry.register(event)
}

/*

  TODO rewrite
  put features in feature package
  rewrite all other parts
  continue with stuff below
  rename events server to events

  TODO improvements
  - make tent tabbed gui
  -- release
  - campfire no gui for food, but put it on direcly, and take it off direcly
  - cmapfire no gui for coal, just put it in
  - campfire no gui for kits, just right click with kit to put on campfire, kit becomes empty kit, campfire changes state or if already, then kits switch
  - 'spawner' blocks for special cooking fires, spit, grill, pan in creative tab
  -- release
  - trap no gui, just put lure in direcly, just take one max
  - bear model uses polar bear model
  - make sure achievements require the previous
  - unlock all recipes before crafting using advancements
  - add recipe book to camping inventory
  - update website, all recipes including spit stuff in cookbook
  - camping inv tab align to side
  -- release
  - loclization add for some revise current (such as in tent gui)
  - extensive bug test and fix all github bugs left if any
  - campfire cooking turn on off not burn immediatly
  -- release

  TODO new features
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
  - advancements should also be easy to get especially the first one, latter once can of course hard
  - improve world gen, use much more block, more variation
  - entities: smarter, fox: not only arctic, make polar bear drop fur as well
  - item ideas:
    - first aid kit (to heal and cure bleeding), also add bleeding effects when attaked by a bear, or arrows
    - ropes (for climbing on mountains)
  - improve tent system, but ugly now, also perhaps tens build a bit more interestingly in the world directly (scalable :P)
  - stuff arround items, blocks: advnacements make exciting journy like goals; specific tracks
 */