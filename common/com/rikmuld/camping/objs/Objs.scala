package com.rikmuld.camping.objs

import net.minecraft.util.DamageSource
import net.minecraft.client.settings.KeyBinding
import net.minecraft.stats.Achievement
import com.rikmuld.corerm.objs.RMCoreItem
import com.rikmuld.corerm.bounds.BoundsStructure
import com.rikmuld.corerm.objs.RMCoreBlock
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemArmor.ArmorMaterial
import net.minecraft.inventory.ContainerBrewingStand.Potion
import com.rikmuld.camping.misc.CookingEquipment
import net.minecraft.potion.Potion

object Objs {
  var tab:CreativeTabs = _
  
  var fur: ArmorMaterial = _
  
  var knife, 
      parts, 
      backpack, 
      kit, 
      marshmallow, 
      animalParts, 
      furBoot, 
      furLeg, 
      furChest, 
      furHead, 
      venisonCooked, 
      venisonRaw:RMCoreItem = _
      
  var hemp, 
      campfireWood, 
      lantern, 
      tent, 
      logseat, 
      light, 
      sleepingBag, 
      trap, 
      //campfire, 
      campfireCook, 
      tentBounds:RMCoreBlock = _
      
  var guiBackpack, 
      guiConfig, 
      guiKit, 
      guiCamping, 
      guiTrap, 
      guiCampfireCook, 
      guiTentSleep, 
      guiTentChests, 
      guiTentLantern, 
      guiTent:Int = _
      
  var spit, 
      grill, 
      pan:CookingEquipment = _
      
  var bleeding: Potion = _
  
  var bleedingSource: DamageSource = _
  
  var tentStructure:Array[BoundsStructure] = _
  
  var keyOpenCamping:KeyBinding = _
  
  var achKnife, 
      achCamperFull, 
      achExplorer, 
      achWildMan, 
      achBackBasic, 
      achLuxury, 
      achMarshRoast, 
      achMadCamper, 
      achCampfire, 
      achHunter, 
      achProtector:Achievement = _
}