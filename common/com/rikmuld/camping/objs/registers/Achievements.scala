package com.rikmuld.camping.objs.registers

import com.rikmuld.corerm.objs.RMAchievement
import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.init.Items._
import net.minecraft.init.Blocks._

object ModAchievements extends ModRegister {
  import com.rikmuld.camping.Lib.AchievementInfo._
  
  override def register {      
    achKnife       = RMAchievement.addAchievement(MOD_ID, KNIFE_GET, 0, 0, nwsk(knife), None)
    achCamperFull  = RMAchievement.addAchievement(MOD_ID, FULL_CAMPER, 2, 0, nwsk(backpack), Some(achKnife))
    achExplorer    = RMAchievement.addAchievement(MOD_ID, EXPLORER, 2, -2, nwsk(MAP), Some(achCamperFull))
    achWildMan     = RMAchievement.addAchievement(MOD_ID, WILD_MAN, 4, 0, nwsk(furChest), Some(achCamperFull))
    achBackBasic   = RMAchievement.addAchievement(MOD_ID, TENT_SLEEP, 0, -2, nwsk(tent, 15), Some(achKnife))
    achLuxury      = RMAchievement.addAchievement(MOD_ID, LUXURY_TENT, 0, -4, nwsk(lantern), Some(achBackBasic))
    achMarshRoast  = RMAchievement.addAchievement(MOD_ID, MARSHMELLOW, -2, 0, nwsk(marshmallow), Some(achKnife))
    achMadCamper   = RMAchievement.addAchievement(MOD_ID, MAD_CAMPER, -2, -2, nwsk(DYE, 6), Some(achMarshRoast))
    achCampfire    = RMAchievement.addAchievement(MOD_ID, CAMPFIRE_MASTERY, -4, 0, nwsk(kit), Some(achMarshRoast))
    achProtector   = RMAchievement.addAchievement(MOD_ID, PROTECTOR, 2, 2, nwsk(trap), Some(achKnife))
    achHunter      = RMAchievement.addAchievement(MOD_ID, HUNTER, -2, 2, nwsk(trap), Some(achKnife))
    
    achLuxury.setSpecial
    achWildMan.setSpecial
    achCampfire.setSpecial
    
    RMAchievement.buildPage(MOD_ID, "Camping Mod")
  }
}