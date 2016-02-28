package com.rikmuld.camping.objs.registers

import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.ItemDefinitions._
import net.minecraft.util.BlockPos
import com.rikmuld.camping.objs.item.Kit
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.objs.RMItemArmor
import com.rikmuld.corerm.objs.RMItemFood
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.util.EnumFacing
import com.rikmuld.corerm.objs.RMItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import com.rikmuld.corerm.misc.Rotation

object ModItems extends ModRegister {   
  override def register {      
    knife = new RMItem(MOD_ID, KNIFE){
      override def onItemUse(item: ItemStack, player: EntityPlayer, world: World, pos:BlockPos, sideHit: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
        !world.isRemote && Rotation.rotateBlock(world, pos)
      }
    }
    parts = new RMItem(MOD_ID, PARTS)
    marshmallow = new RMItemFood(MOD_ID, MARSHMALLOW){
        override def onFoodEaten(stack: ItemStack, world: World, player: EntityPlayer) {
        if (!world.isRemote && (!player.inventory.addItemStackToInventory(nwsk(parts, 1, Parts.STICK_IRON)))) {
          player.dropPlayerItemWithRandomChoice(nwsk(parts, 1, Parts.STICK_IRON), false)
        }
      }
    }
    backpack = new RMItem(MOD_ID, BACKPACK)
    kit = new Kit(MOD_ID, KIT)
          
    if(!config.coreOnly){
      animalParts = new RMItem(MOD_ID, PARTS_ANIMAL)
      venisonRaw = new RMItemFood(MOD_ID, VENISON_RAW)
      venisonCooked = new RMItemFood(MOD_ID, VENISON_COOKED)
      furBoot = new RMItemArmor(MOD_ID, FUR_BOOT)
      furLeg = new RMItemArmor(MOD_ID, FUR_LEG)
      furChest = new RMItemArmor(MOD_ID, FUR_CHEST)
      furHead = new RMItemArmor(MOD_ID, FUR_HEAD)
    }
  }
}