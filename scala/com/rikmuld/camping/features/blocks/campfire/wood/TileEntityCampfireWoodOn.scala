package com.rikmuld.camping.features.blocks.campfire.wood

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Definitions.CampfireWood._
import com.rikmuld.camping.features.blocks.campfire.Roaster
import com.rikmuld.corerm.objs.blocks.BlockSimple
import com.rikmuld.corerm.tileentity.{TileEntitySimple, TileEntityTicker}
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.potion.{Potion, PotionEffect}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World

import scala.collection.JavaConversions._

object TileEntityCampfireWoodOn {
  lazy val effects: Seq[PotionEffect] =
    for (i <- Seq(20, 5, 19, 11, 16, 9, 1, 18, 15, 10, 17, 3, 13, 10, 12))
      yield new PotionEffect(Potion.getPotionById(i), 400, 0)
}

class TileEntityCampfireWoodOn extends TileEntitySimple with TileEntityTicker with Roaster {
  registerTicker(updateEffects, 100)

  def getEntityBounds: AxisAlignedBB =
    getBlock.getBoundingBox(world.getBlockState(pos), world, pos).offset(pos).grow(8)

  def updateEffects(): Unit = {
    val light = getBlock.getInt(world, pos, STATE_LIGHT)

    if (!world.isRemote && light < 15) {
      world.getEntitiesWithinAABB(classOf[EntityLivingBase], getEntityBounds).foreach(entity =>
        entity.addPotionEffect(new PotionEffect(TileEntityCampfireWoodOn.effects(light)))
      )
    }
  }

  def getBlock: BlockSimple =
    CampingMod.OBJ.campfireWoodOn

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean =
    oldState.getBlock != newState.getBlock

  override def roastTime(item: ItemStack): Int =
    350
}