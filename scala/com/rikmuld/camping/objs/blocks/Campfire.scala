package com.rikmuld.camping.objs.blocks

import java.util.Random

import com.rikmuld.camping.CampingMod.proxy
import com.rikmuld.camping.Library.AdvancementInfo
import com.rikmuld.camping.objs.Definitions.CampfireCook._
import com.rikmuld.camping.objs.Definitions.CampfireWood._
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks._
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos, RayTraceResult}
import net.minecraft.util.{EnumFacing, EnumHand, EnumParticleTypes}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object Campfire {
  final val CAMPFIRE_WOOD_BOUNDS =
    new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)

  final val CAMPFIRE_COOK_BOUNDS =
    new AxisAlignedBB(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F)

  def particleAnimation(world: World, pos: BlockPos, color: Int, random: Random, flame: Boolean): Unit = {
    val motionY = random.nextFloat * 0.025f + 0.025f

    val particleX = pos.getX + 0.35f + random.nextFloat * 0.3f
    val particleY = pos.getY + 0.1f + random.nextFloat * 0.15f
    val particleZ = pos.getZ + 0.35f + random.nextFloat * 0.3f

    if(flame)
      proxy.spawnFlame(world, particleX, particleY, particleZ, 0, motionY, 0,
        if(color == 15) 16
        else color
      )

    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX, particleY, particleZ, 0, 0.05, 0)
  }
}

class CampfireCook(modId:String, info:ObjDefinition) extends BlockRM(modId, info) {
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos): AxisAlignedBB =
    Campfire.CAMPFIRE_COOK_BOUNDS

  override def getLightValue(state:IBlockState, world: IBlockAccess, pos:BlockPos): Int =
    if(getBool(state, STATE_ON)) 15 else 0

  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state:IBlockState, world: World, pos:BlockPos, random: Random): Unit =
    if (getBool(state, STATE_ON))
      for (_ <- 0 until 3)
        Campfire.particleAnimation(world, pos, 16, random, flame = true)
}

class CampfireWood(modId:String, info: ObjDefinition) extends BlockRM(modId, info) {
  override def getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB =
    Campfire.CAMPFIRE_WOOD_BOUNDS

  override def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item =
    Items.STICK

  override def quantityDropped(random: Random): Int =
    random.nextInt(4) + 1
}


class CampfireWoodOff(modId:String, info: ObjDefinition) extends CampfireWood(modId, info) {
  override def getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos): Int =
    getInt(state, STATE_LIGHT)

  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state: IBlockState, world: World, pos: BlockPos, random: Random): Unit = {
    val light = getInt(state, STATE_LIGHT)

    if(light > 0)
      for (_ <- 0 until (getInt(state, STATE_LIGHT) / 3) + 1)
        Campfire.particleAnimation(world, pos, 0, random, flame = false)
  }

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean =
    if(!world.isRemote && player.getHeldItem(hand).getItem == Items.STICK) {
      val light = getInt(state, STATE_LIGHT)

      if(light + 2 < 15)
        setState(world, pos, STATE_LIGHT, light + 2)
      else
        world.setBlockState(pos,
          ObjRegistry.campfireWoodOn.setState(ObjRegistry.campfireWoodOn.getDefaultState, STATE_LIGHT, 15)
        )

      true
    } else true
}


class CampfireWoodOn(modId:String, info: ObjDefinition) extends CampfireWood(modId, info) {

  override def getPickBlock(state: IBlockState, target: RayTraceResult,
                            world: World, pos: BlockPos, player: EntityPlayer): ItemStack =

    new ItemStack(ObjRegistry.campfireWood)

  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state: IBlockState, world: World, pos: BlockPos, random: Random): Unit =
    for (_ <- 0 until 3)
      Campfire.particleAnimation(world, pos, getInt(state, STATE_LIGHT), random, flame = true)

  override def onBlockActivated(world: World, pos:BlockPos, state:IBlockState, player: EntityPlayer, hand:EnumHand, side: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean =
    if(!world.isRemote) {
      val stack = player.getHeldItem(hand)
      val color = getInt(state, STATE_LIGHT)

      if(stack.getItem == Items.DYE && stack.getItemDamage != color) {
        setState(world, pos, STATE_LIGHT, stack.getItemDamage)
        stack.setCount(stack.getCount - 1)

        TriggerHelper.trigger(AdvancementInfo.DYE_BURNED, player, stack)

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