package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.PartInfo
import com.rikmuld.corerm.common.objs.item.ItemFoodMain
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import com.rikmuld.camping.core.ModInfo

class Marshmallow(infoClass: Class[_]) extends ItemFoodMain(infoClass, Objs.tab, ModInfo.MOD_ID, Objs.config.marshHeal, Objs.config.marshSaturation, false) {
  override def onFoodEaten(stack: ItemStack, world: World, player: EntityPlayer) {
    if (!world.isRemote && (!player.inventory.addItemStackToInventory(new ItemStack(Objs.parts, 1, PartInfo.STICK_IRON)))) {
      player.dropPlayerItemWithRandomChoice(new ItemStack(Objs.parts, 1, PartInfo.STICK_IRON), false);
    }
  }
}