package com.rikmuld.camping.objs.items

import com.rikmuld.camping.objs.Definitions.Parts
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.items.ItemFoodRM
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class Marshmallow(modId:String, info:ObjDefinition) extends ItemFoodRM(modId, info) {
  override def onFoodEaten(stack: ItemStack, worldIn: World, player: EntityPlayer): Unit = {
    val returnStack = new ItemStack(ObjRegistry.parts, 1, Parts.STICK_IRON)

    if (!player.addItemStackToInventory(returnStack))
      player.dropItem(returnStack, false)
  }
}