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