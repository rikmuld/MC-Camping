package com.rikmuld.camping.features.blocks.campfire.wood

import java.util.Random

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.CampfireWood._
import com.rikmuld.camping.Library.AdvancementInfo
import com.rikmuld.camping.features.blocks.campfire.BlockCampfire
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.objs.ObjDefinition
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.math.{BlockPos, RayTraceResult}
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class BlockCampfireWoodOn(modId:String, info: ObjDefinition) extends BlockCampfireWood(modId, info) {
  override def getPickBlock(state: IBlockState, target: RayTraceResult,
                            world: World, pos: BlockPos, player: EntityPlayer): ItemStack =

    new ItemStack(CampingMod.OBJ.campfireWood)

  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state: IBlockState, world: World, pos: BlockPos, random: Random): Unit =
    for (_ <- 0 until 3)
      BlockCampfire.particleAnimation(world, pos, getInt(state, STATE_LIGHT), random, flame = true)

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean =
    if(!world.isRemote) {
      val stack = player.getHeldItem(hand)
      val color = getInt(state, STATE_LIGHT)

      if(stack.getItem == Items.DYE && stack.getItemDamage != color) {
        setState(world, pos, STATE_LIGHT, stack.getItemDamage)
        stack.setCount(stack.getCount - 1)

        TriggerHelper.trigger(AdvancementInfo.DYE_BURNED, player, stack.getItemDamage)

        true
      } else false
    } else true

  override def updateTick(world: World, pos: BlockPos, state: IBlockState, random: Random): Unit = {
    val color = getInt(state, STATE_LIGHT)

    if (color != 15) {
      setState(world, pos, STATE_LIGHT, 15)

      if (random.nextFloat < 0.10)
        world.destroyBlock(pos, true)

    } else if (random.nextFloat < 0.05)
      world.destroyBlock(pos, true)
  }
}