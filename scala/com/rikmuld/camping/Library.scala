package com.rikmuld.camping

import com.rikmuld.camping.CampingMod.MOD_ID
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard

object Library {
  object ConfigInfo {
    final val CAT_ANIMALS = "animals"
    final val CAT_WORLD = "world genaration"
    final val CAT_TOOLS = "tools"
    final val CAT_FOOD = "food"
    final val CAT_GENERAL = "general"
    final val CAT_CAMPFIRE = "campfire"
  }

  object EntityInfo {
    final val BEAR = "bearGrizzly"
    final val FOX = "foxArctic"
    final val CAMPER = "camper"
    final val MOUNTABLE = "mountable"
  }

  object NBTInfo {
    final val INV_CAMPING = "campInv"
    final val ACHIEVEMENTS = "camping:achievements"
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
    final val MOD_CAT = "The Camping Mod"

    final val INVENTORY_KEY = 0
    final val INVENTORY_DESC = "Camping Inventory Key"
    final val INVENTORY_DEFAULT = Keyboard.KEY_G
  }

  object AdvancementInfo {
    final val SLEPT = new ResourceLocation(MOD_ID, "slept")
    final val CAMPER_INTERACT = new ResourceLocation(MOD_ID, "camper_interact")
    final val TENT_CHANGED = new ResourceLocation(MOD_ID, "tent_changed")
    final val INVENTORY_CHANGED = new ResourceLocation(MOD_ID, "inventory_changed")
    final val FOOD_ROASTED = new ResourceLocation(MOD_ID, "food_roasted")
    final val ENTITY_TRAPPED = new ResourceLocation(MOD_ID, "entity_trapped")
    final val DYE_BURNED = new ResourceLocation(MOD_ID, "dye_burned")
    final val CAMPFIRES_MADE = new ResourceLocation(MOD_ID, "campfires_made")
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
    final val GUI_SIMPLE = GUI_LOCATION + "simple.png"
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

  object GuiInfo {
    final val POUCH = new ResourceLocation(MOD_ID, "pouch")
    final val BACKPACK = new ResourceLocation(MOD_ID, "backpack")
    final val RUCKSACK = new ResourceLocation(MOD_ID, "rucksack")
    final val KIT = new ResourceLocation(MOD_ID, "kit")
    final val CAMPING = new ResourceLocation(MOD_ID, "camping")
    final val TRAP = new ResourceLocation(MOD_ID, "trap")
    final val CAMPFIRE_COOK = new ResourceLocation(MOD_ID, "campfire_cook")
    final val TENT = new ResourceLocation(MOD_ID, "tent")
    final val TENT_SLEEP = new ResourceLocation(MOD_ID, "tent_sleep")
    final val TENT_CHESTS = new ResourceLocation(MOD_ID, "tent_chests")
    final val TENT_LANTERNS = new ResourceLocation(MOD_ID, "tent_lanterns")
    final val CONFIG = new ResourceLocation(MOD_ID, "config")
  }
}
