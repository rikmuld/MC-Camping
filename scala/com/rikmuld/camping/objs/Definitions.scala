package com.rikmuld.camping.objs

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.objs.block.{LanternItem, SleepingBagItem, TentItem}
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.Properties._
import net.minecraft.block.material.Material
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot

object BlockDefinitions {
  final val LANTERN        = new ObjInfo(Tab(tab), 
                                         Name("lantern"),
                                         Materia(Material.GLASS),
                                         Metadata("lantern_off", "lantern_on"),
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
                                         Materia(Material.PLANTS))
  
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
                                         StepSound(SoundEvents.BLOCK_WOOD_STEP), 
                                         Hardness(2.0f), 
                                         HarvestLevel("axe", 0))
   
  final val LIGHT          = new ObjInfo(Name("light"), 
                                         Materia(Material.AIR), 
                                         LightLevel(1.0f))
  
  final val SLEEPING_BAG   = new ObjInfo(Tab(tab), 
                                         Name("sleeping_bag"),
                                         Materia(Material.CLOTH), 
                                         Hardness(0.1f), 
                                         MaxStacksize(4), 
                                         ItemBl(classOf[SleepingBagItem]))
  
  final val TRAP           = new ObjInfo(Tab(tab), 
                                         Name("trap"), 
                                         Materia(Material.IRON), 
                                         Hardness(1f), 
                                         GuiTrigger(Guis.TRAP))
  
  final val CAMPFIRE       = new ObjInfo(Tab(tab), 
                                         Name("campfire"), 
                                         Materia(Material.FIRE), 
                                         Hardness(2f), 
                                         StepSound(SoundEvents.BLOCK_STONE_STEP), 
                                         LightLevel(1.0f))
  
  final val CAMPFIRE_WOOD  = new ObjInfo(Tab(tab), 
                                         Name("campfire_wood"),
                                         Materia(Material.FIRE), 
                                         Hardness(1f), 
                                         StepSound(SoundEvents.BLOCK_STONE_STEP),
                                         LightLevel(1.0f),
                                         HarvestLevel("axe", 0))
  
  final val CAMPFIRE_COOK  = new ObjInfo(Tab(tab),
                                         Name("campfire_cook"),
                                         Materia(Material.FIRE), 
                                         Hardness(2f), 
                                         StepSound(SoundEvents.BLOCK_STONE_STEP), 
                                         LightLevel(1.0f), 
                                         HarvestLevel("pickaxe", 0),
                                         GuiTrigger(Guis.CAMPFIRE_COOK))
  
  final val TENT           = new ObjInfo(Tab(tab), 
                                         Name("tent"),
                                         Materia(Material.CLOTH), 
                                         Hardness(0.2f), 
                                         GuiTrigger(Guis.TENT),
                                         Metadata("black", "red", "green", "brown", "blue", "purple", "cyan", "gray_light", "gray_dark", "pink", "lime", "yellow", "blue_light", "magenta", "orange", "white"),
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
  
  final val BOUNDS_TENT   = new ObjInfo(Name("tent_bounds"),
                                         Materia(Material.CLOTH))
}

object ItemDefinitions {
  final val KNIFE          = new ObjInfo(Tab(tab),
                                         MaxDamage(200),
                                         MaxStacksize(1),
                                         Name("knife"))
    
  final val PARTS          = new ObjInfo(Tab(tab),
                                         Name("parts"), 
                                         Metadata("canvas", "stick_iron", "peg", "pan", "ash", "marshmallows", "marshmallow_stick_raw"))
    
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
                                         GuiTriggerMeta((0, Guis.POUCH), (1, Guis.BACKPACK), (2, Guis.RUCKSACK)),
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
                                         GuiTrigger(Guis.KIT),
                                         Metadata("kit", "spit", "grill", "pan", "useless"))
  object Kit {
    final val EMPTY   = 0
    final val SPIT    = 1
    final val GRILL   = 2
    final val PAN     = 3
    final val USELESS = 4
  }
  
  final val MARSHMALLOW    = new ObjInfo(Tab(tab), 
                                         Name("marshmallow_stick_cooked"),
                                         WolfMeat(false),
                                         Heal(3),
                                         Saturation(1f))
  
  final val PARTS_ANIMAL   = new ObjInfo(Tab(tab), 
                                         Name("parts_animal"),
                                         Metadata("fur_white", "fur_brown"))
    
  object PartsAnimal {
    final val FUR_WHITE = 0
    final val FUR_BROWN = 1
  }
  
  final val FUR_BOOT       = new ObjInfo(Tab(tab), 
                                         Name("armor_fur_boots"),
                                         ArmorTyp(EntityEquipmentSlot.FEET), 
                                         ArmorMateria(fur), 
                                         ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))

  final val FUR_LEG        = new ObjInfo(Tab(tab),
                                         Name("armor_fur_leg"),
                                         ArmorTyp(EntityEquipmentSlot.LEGS), 
                                         ArmorMateria(fur), 
                                         ArmorTexture(TextureInfo.ARMOR_FUR_LEG))
    
  final val FUR_CHEST      = new ObjInfo(Tab(tab), 
                                         Name("armor_fur_chest"),
                                         ArmorTyp(EntityEquipmentSlot.CHEST),
                                         ArmorMateria(fur), 
                                         ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))
  
  final val FUR_HEAD       = new ObjInfo(Tab(tab), 
                                         Name("armor_fur_helm"),
                                         ArmorTyp(EntityEquipmentSlot.HEAD), 
                                         ArmorMateria(fur), 
                                         ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))
      
  final val VENISON_RAW    = new ObjInfo(Tab(tab), 
                                         Name("venison_raw"),
                                         WolfMeat(true),
                                         Heal(4),
                                         Saturation(2f))

  final val VENISON_COOKED = new ObjInfo(Tab(tab), 
                                         Name("venison_cooked"),
                                         WolfMeat(true),
                                         Heal(10),
                                         Saturation(14f))
}