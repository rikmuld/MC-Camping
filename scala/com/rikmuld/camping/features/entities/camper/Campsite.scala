package com.rikmuld.camping.features.entities.camper

import com.rikmuld.camping.features.blocks.tent.BlockTent
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object Campsite {
  def fromNBT(camper:EntityCamper, tag:NBTTagCompound):Campsite = {
    val center = tag.getIntArray("center")
    val tent = tag.getIntArray("tent")
    new Campsite(camper, new BlockPos(center(0), center(1), center(2)), new BlockPos(tent(0), tent(1), tent(2)))
  }
}

class Campsite(camper:EntityCamper, center:BlockPos, tent:BlockPos) {
  def update(): Unit = {
    if(!getWorld.getBlockState(tent).getBlock.isInstanceOf[BlockTent]) camper.setCampsite(None)
    else if(Math.sqrt(camper.getDistanceSqToCenter(center)) > 15 && !camper.getMoveHelper.isUpdating)camper.getMoveHelper.setMoveTo(center.getX, center.getY, center.getZ, .8)
  }
  def getWorld: World = camper.world
  def toNBT(tag:NBTTagCompound):NBTTagCompound = {
    tag.setIntArray("center", Array(center.getX, center.getY, center.getZ))
    tag.setIntArray("tent", Array(tent.getX, tent.getY, tent.getZ))
    tag
  }
}