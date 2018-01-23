package com.rikmuld.camping.objs

import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.corerm.features.bounds.BoundsStructure
import com.rikmuld.corerm.objs.blocks.RMCoreBlock
import com.rikmuld.corerm.objs.items.RMCoreItem
import com.rikmuld.corerm.utils.DataContainer
import net.minecraft.client.settings.KeyBinding
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemArmor.ArmorMaterial
import net.minecraft.potion.Potion
import net.minecraft.stats.Achievement
import net.minecraft.util.{DamageSource, SoundEvent}

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
      
  val guiPouch,
      guiBackpack,
      guiRucksack, 
      guiConfig, 
      guiKit, 
      guiCamping, 
      guiTrap, 
      guiCampfireCook, 
      guiTentSleep, 
      guiTentChests, 
      guiTentLantern, 
      guiTent: DataContainer[Int] = new DataContainer[Int]()
      
  var spit, 
      grill, 
      pan:CookingEquipment = _
      
  var bleeding: Potion = _
  
  var bleedingSource: DamageSource = _
  
  var tentStructure:Array[BoundsStructure] = _
  
  var keyOpenCamping:KeyBinding = _
  
  var foxAmb,
      foxDeath,
      bearAmb,
      bearDeath:SoundEvent = _
  
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