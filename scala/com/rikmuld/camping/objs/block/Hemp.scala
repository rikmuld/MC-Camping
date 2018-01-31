package com.rikmuld.camping.objs.block

import java.util.Random

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.misc.WorldBlock.{BlockData, _}
import com.rikmuld.camping.objs.BlockDefinitions
import com.rikmuld.camping.objs.block.Hemp._
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.util.{BlockRenderLayer, EnumBlockRenderType, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.{EnumPlantType, IPlantable}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

object Hemp {
  val AGE = PropertyInteger.create("age", 0, 5);
}

class Hemp(modId:String, info:ObjInfo) extends RMBlock(modId, info) with IPlantable with WithModel with WithInstable with WithProperties {
  setTickRandomly(true)
  setDefaultState(getStateFromMeta(0))

  override def getRenderType(state:IBlockState) = EnumBlockRenderType.MODEL
  override def getProps = Array(new RMIntProp(AGE, 3, 0))
  override def canPlaceBlockAt(world: World, pos:BlockPos): Boolean = canStay(world, pos)
  override def canStay(world: World, pos: BlockPos): Boolean = {
    val bd = (world, pos)
    ((bd.world, bd.pos.down).block == this && bd.down.state.getValue(AGE) == BlockDefinitions.Hemp.GROWN_BIG_BOTTOM) || bd.down.block.canSustainPlant(bd.state, bd.world, bd.pos.down, EnumFacing.UP, this)
  }
  override def getCollisionBoundingBox(state:IBlockState, world: IBlockAccess, pos:BlockPos): AxisAlignedBB = new AxisAlignedBB(0, 0, 0, 0, 0, 0)
  override def getItemDropped(state: IBlockState, random: Random, pInt: Int): Item = Item.getItemFromBlock(this)
  @SideOnly(Side.CLIENT)
  override def getItem(world: World, pos:BlockPos, state:IBlockState): ItemStack = new ItemStack(Item.getItemFromBlock(this))
  override def getPlantType(world: IBlockAccess, pos:BlockPos): EnumPlantType = EnumPlantType.Beach
  override def getPlant(world: IBlockAccess, pos:BlockPos): IBlockState = getDefaultState
  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB = {
    if (getMetaFromState(state) == 4) new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F)
    else if (getMetaFromState(state) == 3) new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.7F, 0.8F, 0.7F)
    else if ((getMetaFromState(state) == 2) || (getMetaFromState(state) == 5)) new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F)
    else if (getMetaFromState(state) == 1) new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.7F, 0.4F, 0.7F)
    else if (getMetaFromState(state) == 0) new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.7F, 0.2F, 0.7F)
    else new AxisAlignedBB(0, 0, 0, 0, 0, 0)
  }
  @SideOnly(Side.CLIENT)
  override def getBlockLayer = BlockRenderLayer.CUTOUT
  override def updateTick(world: World, pos:BlockPos, state:IBlockState, random: Random) {
    super.updateTick(world, pos, state, random)
    if ((world.getLightBrightness(pos.up) * 15) >= 9) {
      val bd = (world, pos)
      val speed = Math.min(25, Math.max(1, getGrowthRate(bd)))
      if (bd.meta < BlockDefinitions.Hemp.GROWN_SMALL) {
        if (random.nextInt((25.0F / speed).toInt + 1) == 0) bd.setState(getStateFromMeta(bd.meta+1), 2)
      } else if (bd.meta == BlockDefinitions.Hemp.GROWN_SMALL) {
        if (random.nextInt((25.0F / speed).toInt + 1) == 0) {
          if ((world, pos.up).block == Blocks.AIR) {
            (world, pos.up).setState(getStateFromMeta(5), 2)
            bd.setState(getStateFromMeta(4), 2)
          }
        }
      }
    }
  }
  override def breakBlock(world:World, pos:BlockPos, state:IBlockState) = {
    if((world, pos.down).meta==BlockDefinitions.Hemp.GROWN_BIG_BOTTOM)(world, pos.down).setState(getStateFromMeta(BlockDefinitions.Hemp.GROWN_SMALL))
    super.breakBlock(world, pos, state)
  }
  def getGrowthRate(bd:BlockData): Float = {
    var water = if (bd.rel(1, -1, 0).material == Material.WATER) 1 else 0
    water += (if (bd.rel(-1, -1, 0).material == Material.WATER) 1 else 0)
    water += (if (bd.rel(0, -1, 1).material == Material.WATER) 1 else 0)
    water += (if (bd.rel(0, -1, -1).material == Material.WATER) 1 else 0)
    var light = Math.max(1, ((bd.world.getLightBrightness(bd.up.pos) * 15) - 9) / 3f)
    var ground = if (bd.down.block == Blocks.GRASS || bd.down.block == Blocks.DIRT) 2 else 1
    ground * water * light * config.hempSpeed
  }
  override def getDrops(world: IBlockAccess, pos:BlockPos, state:IBlockState, fortune: Int): java.util.List[ItemStack] = if (getMetaFromState(state) >= BlockDefinitions.Hemp.GROWN_SMALL) super.getDrops(world, pos, state, fortune) else List()
  def grow(bd:BlockData): Boolean = {
    if (bd.meta < BlockDefinitions.Hemp.GROWN_SMALL) bd.setState(getStateFromMeta(Math.min(BlockDefinitions.Hemp.GROWN_SMALL, bd.meta + new Random().nextInt(3 - bd.meta) + 1)), 2)
    else if (bd.meta == BlockDefinitions.Hemp.GROWN_SMALL) {
      bd.setState(getStateFromMeta(BlockDefinitions.Hemp.GROWN_BIG_BOTTOM), 2)
      (bd.world, bd.pos.up).setState(getStateFromMeta(BlockDefinitions.Hemp.GROWN_BIG_TOP), 2)
      true
    } else false
  }
}