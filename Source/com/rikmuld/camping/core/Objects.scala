package com.rikmuld.camping.core

import com.rikmuld.corerm.core.ObjInfo

class KnifeInfo extends ObjInfo {
  override def NAME = "knife"
}

object PartInfo {
  final val CANVAS = 0
  final val STICK_IRON = 1
  final val PAN = 2
  final val ASH = 3
  final val MARSHMALLOW = 4
  final val MARSHMALLOWSTICK = 5
}

class PartInfo extends ObjInfo {
  override def NAME = "parts"
  override def NAME_META = Array("canvas", "stickIron", "pan", "ash", "marshmallows", "marshmallowStickRaw")
}

class HempItemInfo extends ObjInfo {
  override def NAME = "hempItem"
}

class BoundsHelperInfo extends ObjInfo {
  override def NAME = "boundsHelper"
}

class TentInfo extends ObjInfo {
  override def NAME = "tent"
}

class TrapInfo extends ObjInfo {
  override def NAME = "trap"
}

object SleepingBagInfo {
  final val HEAD = 0
  final val FOOT = 1
}

class SleepingBagInfo extends ObjInfo {
  override def NAME = "sleepingBag"
  override def NAME_META = Array("sleepingBag", "sleepingBag")
}

class HempInfo extends ObjInfo {
  override def NAME = "hemp"
  override def NAME_META = Array("plantHemp_0", "plantHemp_1", "plantHemp_2", "plantHemp_3", "plantHemp_4", "plantHemp_5")
}

class LogInfo extends ObjInfo {
  override def NAME = "logSeat"
}

class CampfireInfo extends ObjInfo {
  override def NAME = "campfire"
}

class ArmorFurBootsInfo extends ObjInfo {
  override def NAME = "armorFurBoots"
}

class ArmorFurLegInfo extends ObjInfo {
  override def NAME = "armorFurLeg"
}

class ArmorFurChestInfo extends ObjInfo {
  override def NAME = "armorFurChest"
}

class ArmorFurHelmInfo extends ObjInfo {
  override def NAME = "armorFurHelm"
}

class CampfireCookInfo extends ObjInfo {
  override def NAME = "campfireCook"
}

class LightInfo extends ObjInfo {
  override def NAME = "light"
}

class BackpackInfo extends ObjInfo {
  override def NAME = "backpack"
}

class MarshMallowInfo extends ObjInfo {
  override def NAME = "marshmallowStickCooked"
}

object LanternInfo {
  final val LANTERN_ON = 0
  final val LANTERN_OFF = 1
}

object AnimalPartInfo {
  final val FUR_WHITE = 0
  final val FUR_BROWN = 1
}

class AnimalPartInfo extends ObjInfo {
  override def NAME = "animalParts"
  override def NAME_META = Array("furWhite", "furBrown")
}

class VenisonRawInfo extends ObjInfo {
  override def NAME = "venisonRaw"
}

class VenisonInfo extends ObjInfo {
  override def NAME = "venisonCooked"
}

object KitInfo {
  final val KIT_EMPTY = 0
  final val KIT_SPIT = 1
  final val KIT_GRILL = 2
  final val KIT_PAN = 3
  final val KIT_USELESS = 4
}

class KitInfo extends ObjInfo {
  override def NAME = "kit"
  override def NAME_META = Array("kit", "spitKit", "grillKit", "panKit", "uselessKit")
}

class LanternInfo extends ObjInfo {
  override def NAME = "lantern"
  override def NAME_META = Array("lanternOn", "lanternOff")
}