package com.rikmuld.camping.objs

import com.rikmuld.camping.objs.misc.Tab
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.Properties._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.block.LanternItem
import com.rikmuld.camping.objs.block.TentItem
import com.rikmuld.camping.objs.block.SleepingBagItem
import net.minecraft.block.material.Material
import net.minecraft.block.Block

object BlockDefinitions {
  final val LANTERN        = new ObjInfo(Tab(tab), 
                                         Name("lantern"),
                                         Materia(Material.GLASS),
                                         Metadata("lanternOff", "lanternOn"), 
                                         MaxStacksize(1), 
                                         Hardness(.5f), 
                                         LightOpacity(255), 
                                         ItemBl(classOf[LanternItem]))
  
  object Lantern {
    final val OFF   = 0
    final val ON    = 1
  }
  
  final val HEMP           = new ObjInfo(Tab(tab), 
                                         Name("hemp"), 
                                         Materia(Material.PLANTS), 
                                         Metadata("plantHemp_0", "plantHemp_1", "plantHemp_2", "plantHemp_3", "plantHemp_4", "plantHemp_5"))
  
  object Hemp {
    final val INFANT_1         = 0
    final val INFANT_2         = 1
    final val INFANT_3         = 2
    final val GROWN_SMALL      = 3
    final val GROWN_BIG_BOTTOM = 4
    final val GROWN_BIG_TOP    = 5
  }
  
  final val LOGSEAT        = new ObjInfo(Tab(tab), 
                                         Name("logseat"), 
                                         Materia(Material.WOOD), 
                                         StepSound(Block.soundTypeWood), 
                                         Hardness(2.0f), 
                                         HarvestLevel("axe", 0))
   
  final val LIGHT          = new ObjInfo(Name("light"), 
                                         Materia(Material.AIR), 
                                         LightLevel(1.0f))
  
  final val SLEEPING_BAG   = new ObjInfo(Tab(tab), 
                                         Name("sleepingBag"), 
                                         Materia(Material.CLOTH), 
                                         Hardness(0.1f), 
                                         MaxStacksize(4), 
                                         ItemBl(classOf[SleepingBagItem]))
  
  final val TRAP           = new ObjInfo(Tab(tab), 
                                         Name("trap"), 
                                         Materia(Material.IRON), 
                                         Hardness(1f), 
                                         GuiTrigger(guiTrap))
  
  final val CAMPFIRE       = new ObjInfo(Tab(tab), 
                                         Name("campfire"), 
                                         Materia(Material.FIRE), 
                                         Hardness(2f), 
                                         StepSound(Block.soundTypeStone), 
                                         LightLevel(1.0f))
  
  final val CAMPFIRE_WOOD  = new ObjInfo(Tab(tab), 
                                         Name("campfireWood"), 
                                         Materia(Material.FIRE), 
                                         Hardness(1f), 
                                         StepSound(Block.soundTypeStone),
                                         LightLevel(1.0f),
                                         HarvestLevel("axe", 0))
  
  final val CAMPFIRE_COOK  = new ObjInfo(Tab(tab),
                                         Name("campfireCook"), 
                                         Materia(Material.FIRE), 
                                         Hardness(2f), 
                                         StepSound(Block.soundTypeStone), 
                                         LightLevel(1.0f), 
                                         HarvestLevel("pickaxe", 0),
                                         GuiTrigger(guiCampfireCook))
  
  final val TENT           = new ObjInfo(Tab(tab), 
                                         Name("tent"),
                                         Materia(Material.CLOTH), 
                                         Hardness(0.2f), 
                                         GuiTrigger(guiTent), 
                                         Metadata("black", "red", "green", "brown", "blue", "purple", "cyan", "grayLight", "grayDark", "pink", "lime", "yellow", "blueLight", "magenta", "orange", "white"), 
                                         ItemBl(classOf[TentItem]))
  object Tent {
    final val BLACK       = 0
    final val RED         = 1
    final val GREEN       = 2
    final val BROWN       = 3
    final val BLUE        = 4
    final val PURLE       = 5
    final val CYAN        = 6
    final val GRAY_LIGHT  = 7
    final val GRAY_DRAK   = 8
    final val PINK        = 9
    final val LIME        = 10
    final val YELLOW      = 11
    final val BLUE_LIGHT  = 12
    final val MAGENTA     = 13
    final val ORANGE      = 14
    final val WHITE       = 15
  }
  
  final val BOUNDS_TENT   = new ObjInfo(Name("tentBounds"), 
                                         Materia(Material.CLOTH))
}

object ItemDefinitions {
  final val KNIFE          = new ObjInfo(Tab(tab), 
                                         MaxDamage(config.toolUse), 
                                         MaxStacksize(1), 
                                         Name("knife"))
    
  final val PARTS          = new ObjInfo(Tab(tab),
                                         Name("parts"), 
                                         Metadata("canvas", "stickIron", "peg", "pan", "ash", "marshmallows", "marshmallowStickRaw"))
    
  object Parts {
    final val CANVAS            = 0
    final val STICK_IRON        = 1
    final val TENT_PEG          = 2
    final val PAN               = 3
    final val ASH               = 4
    final val MARSHMALLOW       = 5
    final val MARSHMALLOWSTICK  = 6
  }
  
  final val BACKPACK       = new ObjInfo(Tab(tab), 
                                         MaxStacksize(1), 
                                         Name("bag"), 
                                         ForceSubtype(true), 
                                         GuiTriggerMeta((0, guiPouch), (1, guiBackpack), (2, guiRucksack)),
                                         Metadata("pouch", "backpack", "rucksack"))
  
  object Backpack {
    final val POUCH = 0
    final val BACKPACK = 1
    final val RUCKSACK = 2
  }
  
  final val KIT            = new ObjInfo(Tab(tab), 
                                         MaxStacksize(1), 
                                         Name("kit"), 
                                         ForceSubtype(true), 
                                         GuiTrigger(guiKit), 
                                         Metadata("kit", "spitKit", "grillKit", "panKit", "uselessKit"))
  object Kit {
    final val EMPTY   = 0
    final val SPIT    = 1
    final val GRILL   = 2
    final val PAN     = 3
    final val USELESS = 4
  }
  
  final val MARSHMALLOW    = new ObjInfo(Tab(tab), 
                                         Name("marshmallowStickCooked"), 
                                         WolfMeat(false), 
                                         Heal(config.marshHeal), 
                                         Saturation(config.marshSaturation))
  
  final val PARTS_ANIMAL   = new ObjInfo(Tab(tab), 
                                         Name("partsAnimal"), 
                                         Metadata("furWhite", "furBrown"))
    
  object PartsAnimal {
    final val FUR_WHITE = 0
    final val FUR_BROWN = 1
  }
  
  final val FUR_BOOT       = new ObjInfo(Tab(tab), 
                                         Name("armorFurBoots"), 
                                         ArmorTyp(3), 
                                         ArmorMateria(fur), 
                                         ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))
    
  final val FUR_LEG        = new ObjInfo(Tab(tab), 
                                         Name("armorFurLeg"), 
                                         ArmorTyp(2), 
                                         ArmorMateria(fur), 
                                         ArmorTexture(TextureInfo.ARMOR_FUR_LEG))
    
  final val FUR_CHEST      = new ObjInfo(Tab(tab), 
                                         Name("armorFurChest"), 
                                         ArmorTyp(1),
                                         ArmorMateria(fur), 
                                         ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))
  
  final val FUR_HEAD       = new ObjInfo(Tab(tab), 
                                         Name("armorFurHelm"), 
                                         ArmorTyp(0), 
                                         ArmorMateria(fur), 
                                         ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))
      
  final val VENISON_RAW    = new ObjInfo(Tab(tab), 
                                         Name("venisonRaw"), 
                                         WolfMeat(true), 
                                         Heal(config.venisonRawHeal), 
                                         Saturation(config.venisonRawSaturation))
    
  final val VENISON_COOKED = new ObjInfo(Tab(tab), 
                                         Name("venisonCooked"), 
                                         WolfMeat(true), 
                                         Heal(config.venisonHeal), 
                                         Saturation(config.venisonSaturation))
  
}