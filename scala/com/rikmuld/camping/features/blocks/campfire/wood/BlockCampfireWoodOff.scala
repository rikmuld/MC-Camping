package com.rikmuld.camping.features.blocks.campfire.wood

import java.util.Random

import com.rikmuld.camping.Definitions.CampfireWood._
import com.rikmuld.camping.features.blocks.campfire.BlockCampfire
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.corerm.objs.ObjDefinition
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.util.math.BlockPos
import net.minecraft.util.{EnumFacing, EnumHand}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

class BlockCampfireWoodOff(modId:String, info: ObjDefinition) extends BlockCampfireWood(modId, info) {
  override def getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos): Int =
    getInt(state, STATE_LIGHT)

  @SideOnly(Side.CLIENT)
  override def randomDisplayTick(state: IBlockState, world: World, pos: BlockPos, random: Random): Unit = {
    val light = getInt(state, STATE_LIGHT)

    if(light > 0)
      for (_ <- 0 until (getInt(state, STATE_LIGHT) / 3) + 1)
        BlockCampfire.particleAnimation(world, pos, 0, random, flame = false)
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