package com.rikmuld.camping.features.blocks.campfire.wood

import java.util.Random

import com.rikmuld.camping.features.blocks.campfire.BlockCampfire
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks._
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.IBlockAccess

class BlockCampfireWood(modId:String, info: ObjDefinition) extends BlockRM(modId, info) {
  override def getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB =
    BlockCampfire.CAMPFIRE_WOOD_BOUNDS

  override def getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item =
    Items.STICK

  override def quantityDropped(random: Random): Int =
    random.nextInt(4) + 1
}