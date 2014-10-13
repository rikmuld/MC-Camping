package com.rikmuld.camping.common.objs.item

import com.rikmuld.camping.core.ObjInfo
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.PartInfo

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class Marshmallow(infoClass: Class[ObjInfo]) extends ItemFoodMain(infoClass, Objs.config.marshHeal, Objs.config.marshSaturation, false) {
  override def onFoodEaten(stack:ItemStack, world:World, player:EntityPlayer){
    if(!world.isRemote&&(!player.inventory.addItemStackToInventory(new ItemStack(Objs.parts, 1, PartInfo.STICK_IRON))))
    {
    	player.dropPlayerItemWithRandomChoice(new ItemStack(Objs.parts, 1, PartInfo.STICK_IRON), false);
    }
  }
}