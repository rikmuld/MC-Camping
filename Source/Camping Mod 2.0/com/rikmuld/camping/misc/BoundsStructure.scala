package com.rikmuld.camping.misc

import com.rikmuld.camping.common.objs.tile.TileEntityWithBounds
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import scala.collection.mutable.HashMap
import net.minecraft.block.Block
import com.rikmuld.camping.core.Objs
import net.minecraft.init.Blocks
import com.rikmuld.camping.core.Utils._

object Bounds {
  def readBoundsToNBT(tag: NBTTagCompound): Bounds = {
    new Bounds(tag.getFloat("xMin"), tag.getFloat("yMin"), tag.getFloat("zMin"), tag.getFloat("xMax"), tag.getFloat("yMax"), tag.getFloat("zMax"))
  }
}

class Bounds(var xMin: Float, var yMin: Float, var zMin: Float, var xMax: Float, var yMax: Float, var zMax: Float) {
  def setBlockBounds(block: Block) = block.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax)
  def setBlockCollision(block: Block) {
    block.setBlockBounds(Math.max(xMin, 0), Math.max(yMin, 0), Math.max(zMin, 0), Math.min(xMax, 1), Math.min(yMax, 1), Math.min(zMax, 1))
  }
  def writeBoundsToNBT(tag: NBTTagCompound) {
    tag.setFloat("xMin", xMin)
    tag.setFloat("yMin", yMin)
    tag.setFloat("zMin", zMin)
    tag.setFloat("xMax", xMax)
    tag.setFloat("yMax", yMax)
    tag.setFloat("zMax", zMax)
  }
  override def toString() = super.toString + ":" + xMin + ":" + yMin + ":" + zMin + ":" + xMax + ":" + yMax + ":" + zMax;
}

object BoundsStructure {
  def regsisterStructure(xCoords: Array[Int], yCoords: Array[Int], zCoords: Array[Int], rotation: Boolean): Array[BoundsStructure] = {
    if (!rotation) Array(new BoundsStructure(Array(xCoords, yCoords, zCoords)))
    else {
      val structure = Array.ofDim[BoundsStructure](4)
      structure(0) = new BoundsStructure(Array(xCoords, yCoords, zCoords))
      structure(1) = new BoundsStructure(Array(zCoords.inverse, yCoords, xCoords.inverse))
      structure(2) = new BoundsStructure(Array(zCoords.inverse, yCoords, xCoords.inverse))
      structure(3) = new BoundsStructure(Array(zCoords, yCoords, xCoords))
      structure
    }
  }
}

class BoundsStructure(var blocks: Array[Array[Int]]) {
  def canBePlaced(world: World, tracker: BoundsTracker): Boolean = {
    for (i <- 0 until blocks(0).length) {
      val block = world.getBlock(tracker.baseX + blocks(0)(i), tracker.baseY + blocks(1)(i), tracker.baseZ + blocks(2)(i))
      if ((block != Blocks.air) && !(block.isReplaceable(world, tracker.baseX + blocks(0)(i), tracker.baseY + blocks(1)(i), tracker.baseZ + blocks(2)(i)))) return false
    }
    if (hadSolidUnderGround(world, tracker)) true else false
  }
  def createStructure(world: World, tracker: BoundsTracker) {
    for (i <- 0 until blocks(0).length) {
      world.setBlock(tracker.baseX + blocks(0)(i), tracker.baseY + blocks(1)(i), tracker.baseZ + blocks(2)(i), Objs.bounds, 0, 2)
      world.getTileEntity(tracker.baseX + blocks(0)(i), tracker.baseY + blocks(1)(i), tracker.baseZ + blocks(2)(i)).asInstanceOf[TileEntityWithBounds].setBounds(tracker.getBoundsOnRelativePoistion(blocks(0)(i), blocks(1)(i), blocks(2)(i)))
      world.getTileEntity(tracker.baseX + blocks(0)(i), tracker.baseY + blocks(1)(i), tracker.baseZ + blocks(2)(i)).asInstanceOf[TileEntityWithBounds].setBaseCoords(tracker.baseX, tracker.baseY, tracker.baseZ)
    }
  }
  def destroyStructure(world: World, tracker: BoundsTracker) = {
    for (i <- 0 until blocks(0).length) world.setBlock(tracker.baseX + blocks(0)(i), tracker.baseY + blocks(1)(i), tracker.baseZ + blocks(2)(i), Blocks.air)
  }
  def hadSolidUnderGround(world: World, tracker: BoundsTracker): Boolean = {
    (0 until blocks(0).length).find(i => !World.doesBlockHaveSolidTopSurface(world, tracker.baseX + blocks(0)(i), tracker.baseY - 1, tracker.baseZ + blocks(2)(i))).map(_ => false).getOrElse(true)
  }
}

class BoundsTracker(var baseX: Int, var baseY: Int, var baseZ: Int, var bounds: Bounds) {
  def getBoundsOnRelativePoistion(xDiv: Int, yDiv: Int, zDiv: Int): Bounds = new Bounds(bounds.xMin - xDiv, bounds.yMin - yDiv, bounds.zMin - zDiv, bounds.xMax - xDiv, bounds.yMax - yDiv, bounds.zMax - zDiv)
}
