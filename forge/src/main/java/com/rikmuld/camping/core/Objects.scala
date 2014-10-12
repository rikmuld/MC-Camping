package com.rikmuld.camping.core

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

class HempInfo extends ObjInfo {
  override def NAME = "hemp"
}

class CampfireInfo extends ObjInfo {
  override def NAME = "campfire"
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

trait ObjInfo {
  def NAME: String = null
  def NAME_META: Array[String] = null
}