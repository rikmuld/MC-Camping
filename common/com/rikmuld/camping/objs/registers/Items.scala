package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.ItemDefinitions._
import net.minecraft.util.math.BlockPos
import com.rikmuld.camping.objs.item.Kit
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.corerm.objs.RMItemArmor
import com.rikmuld.corerm.objs.RMItemFood
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.corerm.RMMod
import net.minecraft.util.EnumFacing
import com.rikmuld.corerm.objs.RMItem
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.world.World
import com.rikmuld.corerm.misc.Rotation
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumActionResult
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent

object ModItems {
  def createItems() {
    knife = new RMItem(MOD_ID, KNIFE){
      override def onItemUse(player: EntityPlayer, world: World, pos:BlockPos, hand:EnumHand, sideHit: EnumFacing, xHit: Float, yHit: Float, zHit: Float): EnumActionResult = {
        if(!world.isRemote && Rotation.rotateBlock(world, pos)) EnumActionResult.SUCCESS
        else EnumActionResult.PASS
      }
    }
    parts = new RMItem(MOD_ID, PARTS)
    marshmallow = new RMItemFood(MOD_ID, MARSHMALLOW){
        override def onFoodEaten(stack: ItemStack, world: World, player: EntityPlayer) {
        if (!world.isRemote && (!player.inventory.addItemStackToInventory(nwsk(parts, 1, Parts.STICK_IRON)))) {
          player.dropItem(nwsk(parts, 1, Parts.STICK_IRON), false)
        }
      }
    }

    backpack = new RMItem(MOD_ID, BACKPACK)
    kit = new Kit(MOD_ID, KIT)
    animalParts = new RMItem(MOD_ID, PARTS_ANIMAL)
    venisonRaw = new RMItemFood(MOD_ID, VENISON_RAW)
    venisonCooked = new RMItemFood(MOD_ID, VENISON_COOKED)
    furBoot = new RMItemArmor(MOD_ID, FUR_BOOT)
    furLeg = new RMItemArmor(MOD_ID, FUR_LEG)
    furChest = new RMItemArmor(MOD_ID, FUR_CHEST)
    furHead = new RMItemArmor(MOD_ID, FUR_HEAD)
  }

  def registerItems(event: RegistryEvent.Register[Item]): Unit ={
    knife.getItemInfo.register(event, knife, MOD_ID)
    parts.getItemInfo.register(event, parts, MOD_ID)
    marshmallow.getItemInfo.register(event, marshmallow, MOD_ID)
    backpack.getItemInfo.register(event, backpack, MOD_ID)
    kit.getItemInfo.register(event, kit, MOD_ID)
    animalParts.getItemInfo.register(event, animalParts, MOD_ID)
    venisonRaw.getItemInfo.register(event, venisonRaw, MOD_ID)
    venisonCooked.getItemInfo.register(event, venisonCooked, MOD_ID)
    furBoot.getItemInfo.register(event, furBoot, MOD_ID)
    furChest.getItemInfo.register(event, furChest, MOD_ID)
    furHead.getItemInfo.register(event, furHead, MOD_ID)
    furLeg.getItemInfo.register(event, furLeg, MOD_ID)
  }

  def registerModels(event: ModelRegistryEvent): Unit = {
    RMMod.proxy.registerRendersFor(event, knife, knife.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, parts, parts.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, marshmallow, marshmallow.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, backpack, backpack.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, kit, kit.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, animalParts, animalParts.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, venisonCooked, venisonCooked.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, venisonRaw, venisonRaw.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, furLeg, furLeg.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, furHead, furHead.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, furChest, furChest.getItemInfo, MOD_ID)
    RMMod.proxy.registerRendersFor(event, furBoot, furBoot.getItemInfo, MOD_ID)
  }
}