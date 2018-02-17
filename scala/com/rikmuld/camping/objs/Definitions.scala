package com.rikmuld.camping.objs

import com.rikmuld.camping.Lib._
import com.rikmuld.camping.objs.block.Hemp
import com.rikmuld.camping.objs.blocks.{Lantern, LanternItem, Trap}
import com.rikmuld.camping.objs.items.{Kit, Marshmallow}
import com.rikmuld.camping.registers.Objs._
import com.rikmuld.camping.tileentity.{TileLantern, TileLight, TileTrap}
import com.rikmuld.corerm.objs.Properties._
import com.rikmuld.corerm.objs.StateProperty.{PropBool, PropInt}
import com.rikmuld.corerm.objs.{ObjDefinition, States}
import net.minecraft.block.material.Material
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.util.BlockRenderLayer

object Definitions {
  final val LANTERN_STATES = new States(
    PropBool(Lantern.STATE_LIT),
    PropBool(Lantern.STATE_HANGING)
  )

  final val LANTERN = new ObjDefinition(
    Tab(tab),
    Name("lantern"),
    PropMaterial(Material.GLASS),
    ItemMetaData("off", "on"),
    MaxStackSize(1),
    Hardness(Hardness.DIRT),
    BlockStates(LANTERN_STATES),
    Unstable,
    NonCube,
    RenderType(BlockRenderLayer.CUTOUT),
    ItemClass(classOf[LanternItem]),
    BlockClass(classOf[Lantern]),
    TileEntityClass(classOf[TileLantern])
  )

  object Lantern {
    final val OFF = 0
    final val ON = 1

    final val STATE_LIT = "lit"
    final val STATE_HANGING = "hanging"
  }

  final val HEMP_STATES = new States(
    PropInt(Hemp.STATE_AGE, 0, 6)
  )

  final val HEMP = new ObjDefinition(
    Tab(tab),
    Name("hemp"),
    PropMaterial(Material.PLANTS),
    BlockStates(HEMP_STATES),
    Unstable,
    NonCube,
    NoCollision,
    Ticker,
    RenderType(BlockRenderLayer.CUTOUT),
    BlockClass(classOf[Hemp])
  )

  object Hemp {
    final val STATE_AGE = "age"

    final val STATE_AGE_GROWN_BOTTOM = 4
    final val STATE_AGE_GROWN_TOP = 5
    final val STATE_AGE_READY = 3
  }

  //  final val LOGSEAT = new ObjDefinition(
  //    Tab(tab),
  //    Name("logseat"),
  //    PropMaterial(Material.WOOD),
  //    StepType(SoundType.WOOD),
  //    Hardness(Hardness.WOOD),
  //    HarvestLevel(0, "axe")
  //  )
  //

  final val LIGHT = new ObjDefinition(
    Name("light"),
    PropMaterial(Material.AIR),
    LightLevel(1.0f),
    Air,
    TileEntityClass(classOf[TileLight])
  )

  //
  //  final val SLEEPING_BAG = new ObjDefinition(
  //    Tab(tab),
  //    Name("sleeping_bag"),
  //    PropMaterial(Material.CLOTH),
  //    Hardness(0.1f),
  //    MaxStackSize(4),
  //    ItemBlockClass(classOf[SleepingBagItem])
  //  )
  //

  final val TRAP_STATES = new States(
    PropBool(Trap.STATE_OPEN, true)
  )

  final val TRAP = new ObjDefinition(
    Tab(tab),
    Name("trap"),
    PropMaterial(Material.IRON),
    Hardness(Hardness.STONE),
    HarvestLevel(0, "pickaxe"),
    GuiTrigger(GuiInfo.TRAP),
    Unstable,
    NonCube,
    BlockStates(TRAP_STATES),
    TileEntityClass(classOf[TileTrap]),
    BlockClass(classOf[Trap])
  )

  object Trap {
    final val STATE_OPEN = "open"
  }

  //
  //  final val CAMPFIRE_WOOD = new ObjDefinition(
  //    Tab(tab),
  //    Name("campfire_wood"),
  //    PropMaterial(Material.FIRE),
  //    Hardness(1f),
  //    LightLevel(1.0f),
  //    HarvestLevel(0, "axe")
  //  )
  //
  //  final val CAMPFIRE_COOK = new ObjDefinition(
  //    Tab(tab),
  //    Name("campfire_cook"),
  //    PropMaterial(Material.FIRE),
  //    Hardness(Hardness.STONE),
  //    LightLevel(1.0f),
  //    HarvestLevel(0, "pickaxe"),
  //    GuiTrigger(Guis.CAMPFIRE_COOK)
  //  )
  //
  //  final val TENT = new ObjDefinition(
  //    Tab(tab),
  //    Name("tent"),
  //    PropMaterial(Material.CLOTH),
  //    Hardness(0.2f),
  //    GuiTrigger(Guis.TENT),
  //    ItemMetaData("black", "red", "green", "brown", "blue", "purple", "cyan", "gray_light", "gray_dark", "pink", "lime", "yellow", "blue_light", "magenta", "orange", "white"),
  //    ItemBlockClass(classOf[TentItem])
  //  )
  //
  //  object Tent {
  //    final val BLACK = 0
  //    final val RED = 1
  //    final val GREEN = 2
  //    final val BROWN = 3
  //    final val BLUE = 4
  //    final val PURPLE = 5
  //    final val CYAN = 6
  //    final val GRAY_LIGHT = 7
  //    final val GRAY_DARK = 8
  //    final val PINK = 9
  //    final val LIME = 10
  //    final val YELLOW = 11
  //    final val BLUE_LIGHT = 12
  //    final val MAGENTA = 13
  //    final val ORANGE = 14
  //    final val WHITE = 15
  //  }
  //
  //  final val BOUNDS_TENT = new ObjDefinition(
  //    Name("tent_bounds"),
  //    PropMaterial(Material.CLOTH)
  //  )

  final val KNIFE = new ObjDefinition(
    Tab(tab),
    MaxDamage(200),
    MaxStackSize(1),
    Name("knife")
  )

  final val PARTS = new ObjDefinition(
    Tab(tab),
    Name("parts"),
    ItemMetaData("canvas", "stick_iron", "peg", "pan", "ash", "marshmallows", "marshmallow_stick_raw")
  )

  object Parts {
    final val CANVAS = 0
    final val STICK_IRON = 1
    final val TENT_PEG = 2
    final val PAN = 3
    final val ASH = 4
    final val MARSHMALLOW = 5
    final val MARSHMALLOW_STICK = 6
  }

  final val BACKPACK = new ObjDefinition(
    Tab(tab),
    MaxStackSize(1),
    Name("bag"),
    GuiTriggerMeta((0, GuiInfo.POUCH), (1, GuiInfo.BACKPACK), (2, GuiInfo.RUCKSACK)),
    ItemMetaData("pouch", "backpack", "rucksack")
  )

  object Backpack {
    final val POUCH = 0
    final val BACKPACK = 1
    final val RUCKSACK = 2
  }

  final val KIT = new ObjDefinition(
    Tab(tab),
    MaxStackSize(1),
    Name("kit"),
    GuiTrigger(GuiInfo.KIT),
    ItemMetaData("kit", "spit", "grill", "pan", "useless"),
    ItemClass(classOf[Kit])
  )

  object Kit {
    final val EMPTY = 0
    final val SPIT = 1
    final val GRILL = 2
    final val PAN = 3
    final val USELESS = 4
  }

  final val MARSHMALLOW = new ObjDefinition(
    Tab(tab),
    Name("marshmallow_stick_cooked"),
    LikedByWolfs(false),
    FoodPoints(3),
    Saturation(Saturation.Poor),
    ItemClass(classOf[Marshmallow])
  )

  final val PARTS_ANIMAL = new ObjDefinition(
    Tab(tab),
    Name("parts_animal"),
    ItemMetaData("fur_white", "fur_brown")
  )

  object PartsAnimal {
    final val FUR_WHITE = 0
    final val FUR_BROWN = 1
  }

  final val FUR_BOOT = new ObjDefinition(
    Tab(tab),
    Name("armor_fur_boots"),
    ArmorType(EntityEquipmentSlot.FEET),
    PropArmorMaterial(fur),
    ArmorTexture(TextureInfo.ARMOR_FUR_MAIN)
  )

  final val FUR_LEG = new ObjDefinition(
    Tab(tab),
    Name("armor_fur_leg"),
    ArmorType(EntityEquipmentSlot.LEGS),
    PropArmorMaterial(fur),
    ArmorTexture(TextureInfo.ARMOR_FUR_LEG)
  )

  final val FUR_CHEST = new ObjDefinition(
    Tab(tab),
    Name("armor_fur_chest"),
    ArmorType(EntityEquipmentSlot.CHEST),
    PropArmorMaterial(fur),
    ArmorTexture(TextureInfo.ARMOR_FUR_MAIN)
  )

  final val FUR_HEAD = new ObjDefinition(
    Tab(tab),
    Name("armor_fur_helm"),
    ArmorType(EntityEquipmentSlot.HEAD),
    PropArmorMaterial(fur),
    ArmorTexture(TextureInfo.ARMOR_FUR_MAIN)
  )

  final val VENISON_RAW = new ObjDefinition(
    Tab(tab),
    Name("venison_raw"),
    LikedByWolfs(true),
    FoodPoints(4),
    Saturation(Saturation.Low)
  )

  final val VENISON_COOKED = new ObjDefinition(
    Tab(tab),
    Name("venison_cooked"),
    LikedByWolfs(true),
    FoodPoints(10),
    Saturation(Saturation.Good)
  )
}