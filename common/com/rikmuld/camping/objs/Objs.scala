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
import com.rikmuld.corerm.misc.DataContainer
import net.minecraft.potion.Potion
import net.minecraft.util.SoundEvent

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