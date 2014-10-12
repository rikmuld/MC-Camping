package com.rikmuld.camping.common.objs.tile

import java.util.ArrayList
import java.util.Random
import scala.collection.JavaConversions._
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.entity.EntityLivingBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import net.minecraft.util.AxisAlignedBB
import cpw.mods.fml.relauncher.Side

class TileEntityCampfire extends TileEntityWithInventory {
  var color: Int = 16
  var oldTime: Int = _
  var time: Int = _
  var active: Int = 4
  var rand: Random = new Random()
  var coals: Array[Array[Float]] = Array.ofDim[Float](3, 20)
  var effectsOrderd: Array[PotionEffect] = new Array[PotionEffect](16)
  var effectsRaw: Array[PotionEffect] = new Array[PotionEffect](23)

  for (i <- 0 until 23) effectsRaw(i) = new PotionEffect(i, 400, 0)

  effectsOrderd(0) = effectsRaw(20)
  effectsOrderd(1) = effectsRaw(5)
  effectsOrderd(2) = effectsRaw(19)
  effectsOrderd(3) = effectsRaw(11)
  effectsOrderd(4) = effectsRaw(16)
  effectsOrderd(5) = effectsRaw(9)
  effectsOrderd(6) = effectsRaw(1)
  effectsOrderd(7) = effectsRaw(18)
  effectsOrderd(8) = effectsRaw(15)
  effectsOrderd(9) = effectsRaw(10)
  effectsOrderd(10) = effectsRaw(17)
  effectsOrderd(11) = effectsRaw(3)
  effectsOrderd(12) = effectsRaw(13)
  effectsOrderd(13) = effectsRaw(10)
  effectsOrderd(14) = effectsRaw(12)
  effectsOrderd(15) = effectsRaw(14)

  for (i <- 0 until 20) {
    coals(0)(i) = rand.nextFloat() / 5F
    coals(1)(i) = rand.nextFloat() / 5F
    coals(2)(i) = rand.nextFloat() * 360
  }

  private def colorFlame(color: Int) {
    this.color = color
    if (!worldObj.isRemote) sendTileData(0, true, color)
  }
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1)
  override def getSizeInventory(): Int = 1
  override def readFromNBT(tag: NBTTagCompound) {
    color = tag.getInteger("color")
    time = tag.getInteger("time")
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) {
      coals(i)(j) = tag.getFloat("coals" + i + j)
    }
    super.readFromNBT(tag)
  }
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 0) colorFlame(data(0))
    if (id == 1) time = data(0)
  }
  override def updateEntity() {
    if (!worldObj.isRemote) {
      if (active > 0) {
        active -= 1
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
      }
      oldTime = time
      if ((getStackInSlot(0) != null) && (time == 0)) {
        colorFlame(getStackInSlot(0).getItemDamage)
        time = 6000
        decrStackSize(0, 1)
      }
      if (time != 0) time -= 1
      if ((color != 16) && (time == 0)) colorFlame(16)
      if (oldTime != time) sendTileData(1, true, time)
      if ((time > 0) && ((time % 120) == 0)) {
        val entitys = worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], AxisAlignedBB.getBoundingBox(xCoord - 8, yCoord - 8, zCoord - 8, xCoord + 8, yCoord + 8, zCoord + 8)).asInstanceOf[ArrayList[EntityLivingBase]]
        for (entity <- entitys) entity.addPotionEffect(new PotionEffect(effectsOrderd(color)))
      }
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    tag.setInteger("color", color)
    tag.setInteger("time", time)
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) tag.setFloat("coals" + i + j, coals(i)(j))
    super.writeToNBT(tag)
  }
}
