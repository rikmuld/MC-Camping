package com.rikmuld.camping.objs.block

import java.util.Random

import com.rikmuld.camping.objs.Definitions.Hemp._
import com.rikmuld.corerm.objs.ObjDefinition
import com.rikmuld.corerm.objs.blocks.BlockRM
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.{IBlockAccess, World}
import net.minecraftforge.common.{EnumPlantType, IPlantable}

import scala.collection.JavaConversions._

class Hemp(modId:String, info: ObjDefinition) extends BlockRM(modId, info) with IPlantable {
  override def getPlantType(world: IBlockAccess, pos:BlockPos): EnumPlantType =
    EnumPlantType.Beach

  override def getPlant(world: IBlockAccess, pos:BlockPos): IBlockState = {
    val state = world.getBlockState(pos)

    if (state.getBlock == this) state
    else getDefaultState
  }

  override def getBoundingBox(state:IBlockState, source:IBlockAccess, pos:BlockPos):AxisAlignedBB =
    getInt(state, STATE_AGE) match {
      case STATE_AGE_GROWN_TOP =>
        new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F)
      case age =>
        new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.7F, (age + 1) * 0.2F, 0.7F)
    }

  override def canStay(world: World, pos: BlockPos): Boolean = {
    val state = world.getBlockState(pos.down)

    state.getBlock.canSustainPlant(state, world, pos.down, EnumFacing.UP, this)
  }

  override def canSustainPlant(state: IBlockState, world: IBlockAccess, pos: BlockPos,
                               direction: EnumFacing, plantable: IPlantable): Boolean =
    plantable.getPlant(world, pos.offset(direction)).getBlock == this &&
        getInt(state, STATE_AGE) == STATE_AGE_GROWN_BOTTOM

  override def updateTick(world: World, pos:BlockPos, state:IBlockState, random: Random): Unit =
    if ((world.getLightBrightness(pos.up) * 15) >= 9 && random.nextInt(getGrowthDelay(world, pos)) == 0)
      grow(world, pos)

  override def getDrops(world: IBlockAccess, pos:BlockPos,
                        state:IBlockState, fortune: Int): java.util.List[ItemStack] =

    if (getInt(state, STATE_AGE) >= STATE_AGE_READY)
      super.getDrops(world, pos, state, fortune)
    else
      List()


  override def breakBlock(world:World, pos:BlockPos, state:IBlockState): Unit = {
    if(getInt(state, STATE_AGE) == STATE_AGE_GROWN_TOP && world.getBlockState(pos.down).getBlock == this)
      setState(world, pos.down, STATE_AGE, STATE_AGE_GROWN_BOTTOM - 1)

    super.breakBlock(world, pos, state)
  }

  def grow(world: World, pos: BlockPos): Unit = {
    val age = getInt(world.getBlockState(pos), STATE_AGE)

    if (age < STATE_AGE_READY)
      setState(world, pos, STATE_AGE, age + 1)
    else if (age == STATE_AGE_READY && world.isAirBlock(pos.up)) {
      setState(world, pos, STATE_AGE, age + 1)

      world.setBlockState(pos.up,
        setState(getDefaultState, STATE_AGE, STATE_AGE_GROWN_TOP)
      )
    }
  }

  def getGrowthDelay(world: World, pos: BlockPos): Int = {
    val waterCount =
      Seq(pos.down.north, pos.down.west, pos.down.south, pos.down.east).count(pos =>
        world.getBlockState(pos).getMaterial == Material.WATER
      )

    val lightLevel = (world.getLightBrightness(pos.up) * 16).toInt
    val ground = world.getBlockState(pos).getBlock

    val groundMultiplier =
      if(ground == Blocks.GRASS || ground == Blocks.DIRT) 1
      else 2

    ((32 - (lightLevel / 2)) * groundMultiplier  * (1f / waterCount)).toInt
  }
}