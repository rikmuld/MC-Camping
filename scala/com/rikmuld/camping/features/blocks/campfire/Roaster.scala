package com.rikmuld.camping.features.blocks.campfire

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.Parts
import com.rikmuld.camping.Library.AdvancementInfo
import com.rikmuld.camping.features.blocks.logseat.EntityMountable
import com.rikmuld.corerm.advancements.TriggerHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

trait Roaster {
  def canRoast(item: ItemStack): Boolean =
    item.getItem == CampingMod.OBJ.parts &&
      item.getItemDamage == Parts.MARSHMALLOW_STICK

  protected def roastResult(item:ItemStack): ItemStack =
    new ItemStack(CampingMod.OBJ.marshmallow)

  def roastTime(item: ItemStack): Int =
    150

  def roastSpeed(item: ItemStack): Float =
    1

  def roast(player: EntityPlayer, item: ItemStack): Option[ItemStack] =
    if(!canRoast(item)) None
    else {
      val result = roastResult(item)
      val roastTrigger =
        !player.world.isRemote &&
          Option(player.getRidingEntity).isDefined &&
          player.getRidingEntity.isInstanceOf[EntityMountable]

      if (roastTrigger)
        TriggerHelper.trigger(AdvancementInfo.FOOD_ROASTED, player, (item, result))

      Some(result)
    }
}