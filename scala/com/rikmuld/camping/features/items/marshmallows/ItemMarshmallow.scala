package com.rikmuld.camping.features.items.marshmallows

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.Parts
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.items.ItemFoodRM
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemMarshmallow(modId:String, info:ObjDefinition) extends ItemFoodRM(modId, info) {
  override def onFoodEaten(stack: ItemStack, worldIn: World, player: EntityPlayer): Unit = {
    val returnStack = new ItemStack(CampingMod.OBJ.parts, 1, Parts.STICK_IRON)

    if (!player.addItemStackToInventory(returnStack))
      player.dropItem(returnStack, false)
  }
}