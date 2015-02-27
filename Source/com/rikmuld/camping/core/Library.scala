package com.rikmuld.camping.core

import org.lwjgl.input.Keyboard

object ModInfo {
  final val MOD_ID = "camping"
  final val MOD_NAME = "The Camping Mod 2"
  final val MOD_VERSION = "2.1f"
  final val MOD_LANUAGE = "scala"
  final val MOD_DEPENDENCIES = "required-after:Forge@[v10.13.2.1231,);required-after:corerm@[1.0,)"
  final val MOD_SERVER_PROXY = "com.rikmuld.camping.core.ProxyServer"
  final val MOD_CLIENT_PROXY = "com.rikmuld.camping.core.ProxyClient"
  final val MOD_GUIFACTORY = "com.rikmuld.camping.core.GuiFactory"
  final val PACKET_CHANEL = MOD_ID
}

object NBTInfo {
  final val INV_CAMPING = "campInv"
}

object GuiInfo {
  final val GUI_CAMPFIRE = 0
  final val GUI_BACKPACK = 1
  final val GUI_KIT = 2
  final val GUI_CAMPFIRE_COOK = 3
  final val GUI_CAMPINV = 4
  final val GUI_CAMPINV_CRAFT = 6
  final val GUI_TENT = 7
  final val GUI_TENT_SLEEP = 8
  final val GUI_TENT_CHESTS = 9
  final val GUI_TENT_LANTERN = 10
  final val GUI_TRAP = 11
}

object EntityInfo {
  final val BEAR = 0
  final val FOX = 1
  final val CAMPER = 2
}

object PotionInfo {
  final val BLEEDING = "Bleeding"
}

object DamageInfo {
  final val BLEEDING = "bleedingSource"
}

object ConfigInfo {
  final val CAT_ANIMALS = "animals"
  final val CAT_WORLD = "world genaration"
  final val CAT_TOOLS = "tools"
  final val CAT_FOOD = "food"
  final val CAT_GENERAL = "general"
  final val CAT_CAMPFIRE = "campfire"
}

object ModelInfo {
  final val MODEL_LOCATION = ModInfo.MOD_ID + ":misc/models/"

  final val CAMPFIRE = MODEL_LOCATION + "ModelCampfireDeco.tcn"
  final val LOG = MODEL_LOCATION + "ModelLog.tcn"
  final val TENT = MODEL_LOCATION + "ModelTent.tcn"
  final val TRAP_OPEN = MODEL_LOCATION + "ModelTrapOpen.tcn"
  final val TRAP_CLOSED = MODEL_LOCATION + "ModelTrapClosed.tcn"
}

object TextureInfo {
  final val GUI_LOCATION = ModInfo.MOD_ID + ":textures/gui/"
  final val MODEL_LOCATION = ModInfo.MOD_ID + ":textures/models/"
  final val SPRITE_LOCATION = ModInfo.MOD_ID + ":textures/sprite/"
  final val MC_GUI_LOCATION = "minecraft:textures/gui/"

  final val MODEL_CAMPFIRE = MODEL_LOCATION + "ModelCampfireDeco.png"
  final val SPRITE_FX = SPRITE_LOCATION + "SpriteFX.png"
  final val GUI_CAMPFIRE = GUI_LOCATION + "GuiCampfireDeco.png"
  final val GUI_CAMPINV_BACK = GUI_LOCATION + "GuiCampingBackpack.png"
  final val GUI_CAMPINV_TOOL = GUI_LOCATION + "GuiCampingTool.png"
  final val MC_INVENTORY = MC_GUI_LOCATION + "inventory.png"
  final val GUI_BAG = GUI_LOCATION + "GuiBackpack.png"
  final val GUI_KIT = GUI_LOCATION + "GuiKit.png"
  final val GUI_CAMPFIRE_COOK = GUI_LOCATION + "GuiCampfireCook.png"
  final val MODEL_GRILL = MODEL_LOCATION + "ModelGrill.png"
  final val MODEL_PAN = MODEL_LOCATION + "ModelPan.png"
  final val MODEL_LOG = MODEL_LOCATION + "ModelLog.png"
  final val MODEL_LOG2 = MODEL_LOCATION + "ModelLog2.png"
  final val SPRITE_POTION = SPRITE_LOCATION + "SpritePotion.png"
  final val MODEL_SPIT = MODEL_LOCATION + "ModelSpit.png"
  final val GUI_CAMPINV = GUI_LOCATION + "GuiCampinv.png"
  final val GUI_CAMPINV_CRAFT = GUI_LOCATION + "GuiCampinvCraft.png"
  final val RED_DOT = GUI_LOCATION + "GuiMapRed.png"
  final val BLUE_DOT = GUI_LOCATION + "GuiMapBlue.png"
  final val GUI_UTILS = GUI_LOCATION + "GuiUtils.png"
  final val GUI_TRAP = GUI_LOCATION + "GuiTrap.png"
  final val MODEL_SLEEPING_TOP = MODEL_LOCATION + "ModelSleepingBagTop.png"
  final val MODEL_SLEEPING_DOWN = MODEL_LOCATION + "ModelSleepingBagDown.png"
  final val MODEL_TENT_WHITE = MODEL_LOCATION + "ModelTentWhite.png"
  final val GUI_TENT = GUI_LOCATION + "GuiTent.png"
  final val GUI_TENT_CONTENDS_1 = GUI_LOCATION + "GuiTentContend1.png"
  final val GUI_TENT_CONTENDS_2 = GUI_LOCATION + "GuiTentContend2.png"
  final val ARMOR_FUR_LEG = MODEL_LOCATION + "ModelArmorFurLeg.png"
  final val ARMOR_FUR_MAIN = MODEL_LOCATION + "ModelArmorFurMain.png"
  final val MODEL_BEAR = MODEL_LOCATION + "ModelBear.png"
  final val MODEL_FOX = MODEL_LOCATION + "ModelArcticFox.png"
  final val MODEL_TRAP = MODEL_LOCATION + "ModelBearTrapOpen.png"
  final val MODEL_CAMPER_FEMALE = MODEL_LOCATION + "ModelCamperFemale.png"
  final val MODEL_CAMPER_MALE = MODEL_LOCATION + "ModelCamperMale.png"
}

object KeyInfo {
  final val CATAGORY_MOD = "The Camping Mod"
  final val INVENTORY_KEY = 0

  final val desc: Array[String] = Array(
    "Camping Inventory Key")

  final val values: Array[Integer] = Array(
    Keyboard.KEY_C)
}