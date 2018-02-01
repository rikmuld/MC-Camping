package com.rikmuld.camping.registers

import com.rikmuld.camping.misc.{CookingEquipment, TabCamping}
import com.rikmuld.corerm.old.BoundsStructure
import net.minecraft.client.settings.KeyBinding
import net.minecraft.item.ItemArmor.ArmorMaterial
import net.minecraft.potion.Potion
import net.minecraft.util.{DamageSource, SoundEvent}

object Objs {
  var tab: TabCamping = _
  
  var fur: ArmorMaterial = _
  
//  var knife,
//      parts,
//      backpack,
//      kit,
//      marshmallow,
//      animalParts,
//      furBoot,
//      furLeg,
//      furChest,
//      furHead,
//      venisonCooked,
//      venisonRaw:ItemSimple = _
//
//  var hemp,
//      campfireWood,
//      lantern,
//      tent,
//      logseat,
//      light,
//      sleepingBag,
//      trap,
//      campfireCook,
//      tentBounds: BlockSimple = _
//
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
}