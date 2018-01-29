package com.rikmuld.camping.objs.registers

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs._
import com.rikmuld.camping.objs.item.Kit
import com.rikmuld.corerm.objs.items.{RMItem, RMItemArmor, RMItemBlock, RMItemFood}
import com.rikmuld.corerm.utils.CoreUtils._
import com.rikmuld.corerm.utils.Rotation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{EnumActionResult, EnumFacing, EnumHand}
import net.minecraft.world.World
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent

object ModItems {
  var
    campfireCookItem,
    campfireWoodItem,
    sleepingBagItem,
    tentItem,
    logSeatItem,
    lanternItem,
    trapItem,
    hempItem: RMItemBlock = _

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

    campfireCookItem = campfireCook.generateItem()
    campfireWoodItem = campfireWood.generateItem()
    lanternItem = lantern.generateItem()
    logSeatItem = logseat.generateItem()
    tentItem = tent.generateItem()
    trapItem = trap.generateItem()
    sleepingBagItem = sleepingBag.generateItem()
    hempItem = hemp.generateItem()

  }

  def registerItems(event: RegistryEvent.Register[Item]): Unit =
    event.getRegistry.registerAll(
      knife,
      parts,
      marshmallow,
      backpack,
      kit,
      animalParts,
      venisonRaw,
      venisonCooked,
      furBoot,
      furChest,
      furHead,
      furLeg,
      campfireWoodItem,
      campfireCookItem,
      lanternItem,
      logSeatItem,
      trapItem,
      tentItem,
      sleepingBagItem,
      hempItem
    )

  def registerModels(event: ModelRegistryEvent): Unit = {
    knife.registerRenders()
    parts.registerRenders()
    marshmallow.registerRenders()
    backpack.registerRenders()
    kit.registerRenders()
    animalParts.registerRenders()
    venisonRaw.registerRenders()
    venisonCooked.registerRenders()
    furBoot.registerRenders()
    furChest.registerRenders()
    furHead.registerRenders()
    furLeg.registerRenders()
    campfireWoodItem.registerRenders()
    campfireCookItem.registerRenders()
    lanternItem.registerRenders()
    logSeatItem.registerRenders()
    trapItem.registerRenders()
    tentItem.registerRenders()
    sleepingBagItem.registerRenders()
    hempItem.registerRenders()
  }
}