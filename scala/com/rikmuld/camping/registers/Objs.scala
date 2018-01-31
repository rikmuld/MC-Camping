package com.rikmuld.camping.registers

import com.rikmuld.camping.entity.Camper
import com.rikmuld.camping.inventory.camping.InventoryCamping
import com.rikmuld.corerm.advancements.triggers.TriggerSimple
import com.rikmuld.corerm.objs.blocks.BlockSimple
import com.rikmuld.corerm.objs.items.ItemSimple
import com.rikmuld.corerm.old.BoundsStructure
import net.minecraft.client.settings.KeyBinding
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemArmor.ArmorMaterial
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.util.math.BlockPos
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
      venisonRaw:ItemSimple = _
      
  var hemp, 
      campfireWood, 
      lantern, 
      tent, 
      logseat, 
      light, 
      sleepingBag, 
      trap, 
      campfireCook,
      tentBounds: BlockSimple = _
      
//  var spit,
//      grill,
//      pan:CookingEquipment = _
      
  var bleeding: Potion = _
  
  var bleedingSource: DamageSource = _
  
  var tentStructure:Array[BoundsStructure] = _
  
  var keyOpenCamping:KeyBinding = _
  
  var foxAmb,
      foxDeath,
      bearAmb,
      bearDeath:SoundEvent = _

  var slept: TriggerSimple.Trigger[BlockPos, _] = _
  var campfireMade: TriggerSimple.Trigger[Int, _] = _
  var dyeBurned: TriggerSimple.Trigger[Int, _] = _
  var camperInteract: TriggerSimple.Trigger[Camper, _] = _
  var entityTrapped: TriggerSimple.Trigger[EntityLivingBase, _] = _
  var foodRoasted: TriggerSimple.Trigger[(ItemStack, ItemStack), _] = _
  var inventoryChanged: TriggerSimple.Trigger[InventoryCamping, _] = _
  var tentChanged: TriggerSimple.Trigger[Seq[ItemStack], _] = _
}