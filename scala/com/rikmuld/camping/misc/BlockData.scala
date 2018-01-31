package com.rikmuld.camping.misc

import com.rikmuld.corerm.utils.WorldUtils
import net.minecraft.block.state.IBlockState
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object WorldBlock {
  type BlockData = (World, BlockPos)
  type LazyBlockData = (World, BlockPos, IBlockState)

  implicit class IMBlockData(data:BlockData) extends BlockFunctions {
    val world = data._1
    val pos = data._2
    val state = world.getBlockState(pos)
  }

  implicit class IMLazyBlockData(data:LazyBlockData) extends BlockFunctions {
    val world = data._1
    val pos = data._2
    val state = data._3
  }

  trait BlockFunctions {
    val world:World
    val state:IBlockState
    val pos:BlockPos

    def block = state.getBlock
    def material = state.getMaterial
    def meta = block.getMetaFromState(state)
    def tile = world.getTileEntity(pos)
    def x = pos.getX
    def y = pos.getY
    def z = pos.getZ
    def setState(state:IBlockState) = world.setBlockState(pos, state)
    def setMeta(meta:Int) = world.setBlockState(pos, block.getStateFromMeta(meta))
    def relPos(xFlag:Int, yFlag:Int, zFlag:Int) = new BlockPos(x+xFlag, y+yFlag, z+zFlag)
    def rel(xFlag:Int, yFlag:Int, zFlag:Int) = nw(new BlockPos(x+xFlag, y+yFlag, z+zFlag))
    def setState(state:IBlockState, flag:Int) = world.setBlockState(pos, state, flag)
    def newState(meta:Int) = block.getStateFromMeta(meta)
    def notifyWorld = world.notifyNeighborsOfStateChange(pos, block, true)
    def update = world.notifyBlockUpdate(pos, state, state, 3)
    def updateRender = world.markBlockRangeForRenderUpdate(relPos(-1, -1, -1), relPos(1, 1, 1))
    def clearBlock = world.setBlockToAir(pos)
    def dropInvItems = WorldUtils.dropBlockItems(world, pos)
    def isReplaceable = block.isReplaceable(world, pos)
    def solidBelow = world.isSideSolid(relPos(0, -1, 0), EnumFacing.UP)
    def canInstableStand = ((block == null) || isReplaceable) && solidBelow
    def toAir = world.setBlockToAir(pos)
    def isAir = world.isAirBlock(pos)
    def north = nw(pos.north)
    def south = nw(pos.south)
    def west = nw(pos.west)
    def east = nw(pos.east)
    def up = nw(pos.up)
    def down = nw(pos.down)
    def mkLazy = (world, pos, state)
    def unLazy = (world, pos)
    def nw(pos:BlockPos):BlockData = (world, pos)
    def nw(facing:EnumFacing):BlockData = (world, pos.offset(facing))
  }
}