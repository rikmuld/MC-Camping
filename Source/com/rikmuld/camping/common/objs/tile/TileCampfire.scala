package com.rikmuld.camping.common.objs.tile

import java.util.ArrayList
import java.util.Random
import scala.collection.JavaConversions._
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.entity.EntityLivingBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import com.rikmuld.corerm.core.CoreUtils._
import net.minecraft.util.AxisAlignedBB
import cpw.mods.fml.relauncher.Side
import com.rikmuld.camping.core.ObjRegistry
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.core.Objs
import com.rikmuld.camping.core.PartInfo
import com.rikmuld.corerm.common.network.PacketSender
import com.rikmuld.camping.common.inventory.SlotCooking
import com.rikmuld.camping.core.Utils._
import com.rikmuld.camping.core.Config
import com.rikmuld.corerm.common.objs.tile.TileEntityWithInventory
import com.rikmuld.corerm.common.objs.tile.TileEntityMain

class TileEntityCampfire extends TileEntityMain with TileEntityWithInventory {
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
    super.readFromNBT(tag)
    color = tag.getInteger("color")
    time = tag.getInteger("time")
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) {
      coals(i)(j) = tag.getFloat("coals" + i + j)
    }
    super[TileEntityWithInventory].readFromNBT(tag)
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
    super.writeToNBT(tag)
    tag.setInteger("color", color)
    tag.setInteger("time", time)
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) tag.setFloat("coals" + i + j, coals(i)(j))
    super[TileEntityWithInventory].writeToNBT(tag)
  }
}

class TileEntityCampfireCook extends TileEntityMain with TileEntityWithInventory {
  var maxFeul: Int = Objs.config.campfireMaxFuel
  var fuelForCoal: Int = Objs.config.campfireCoalFuel
  var fuel: Int = _
  var coals: Array[Array[Float]] = Array.ofDim[Float](3, 20)
  var rand: Random = new Random()
  var cookProgress: Array[Int] = new Array[Int](10)
  var oldCookProgress: Array[Int] = new Array[Int](10)
  var equipment: CookingEquipment = _
  var slots: ArrayList[SlotCooking] = _
  private var active: Boolean = _
  private var oldActive: Boolean = _
  private var update: Int = _

  for (i <- 0 until 20) {
    coals(0)(i) = rand.nextFloat() / 5F
    coals(1)(i) = rand.nextFloat() / 5F
    coals(2)(i) = rand.nextFloat() * 360
  }

  private def cookFood() {
    if (equipment != null) {
      for (i <- 0 until equipment.maxFood) {
        oldCookProgress(i) = cookProgress(i)
        if (cookProgress(i) >= equipment.cookTime) {
          cookProgress(i) = 0
          if (equipment.canCook(getStackInSlot(i + 2))) {
            if (equipment.getCookedFood(getStackInSlot(i + 2)) != null) {
              setInventorySlotContents(i + 2, equipment.getCookedFood(getStackInSlot(i + 2)).copy())
            } else {
              setInventorySlotContents(i + 2, new ItemStack(Objs.parts, 1, PartInfo.ASH))
            }
          } else {
            setInventorySlotContents(i + 2, new ItemStack(Objs.parts, 1, PartInfo.ASH))
          }
          PacketSender.toClient(new com.rikmuld.camping.common.network.Items(i + 2, xCoord, yCoord, zCoord, getStackInSlot(i + 2)))
        }
        if (fuel > 0) {
          if ((getStackInSlot(i + 2) != null) &&
            (!(getStackInSlot(i + 2).getItem == Objs.parts) ||
              getStackInSlot(i + 2).getItemDamage != PartInfo.ASH)) {
            cookProgress(i) += 1
          }
        } else if (cookProgress(i) > 0) {
          cookProgress(i) = 0
        }
        if ((getStackInSlot(i + 2) == null) && (cookProgress(i) > 0)) {
          cookProgress(i) = 0
        }
        if (oldCookProgress(i) != cookProgress(i)) {
          sendTileData(1, true, cookProgress(i), i)
        }
      }
    }
  }
  def getCoalPieces(): Int = if (fuel > 0) (if (((fuel / fuelForCoal) + 1) <= 20) (fuel / fuelForCoal) + 1 else 20) else 0
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1)
  def getScaledCoal(maxPixels: Int): Float = (fuel.toFloat / maxFeul.toFloat) * maxPixels
  def getScaledcookProgress(maxPixels: Int, foodNum: Int): Float = ((cookProgress(foodNum).toFloat + 1) / equipment.cookTime) * maxPixels
  override def getSizeInventory(): Int = 12
  def manageCookingEquipment() {
    if ((equipment == null) && (getStackInSlot(1) != null)) {
      equipment = CookingEquipment.getCooking(getStackInSlot(1))
    } else if ((equipment != null) && (getStackInSlot(1) == null)) {
      equipment = null
    }
    if (slots != null) {
      if (equipment != null) {
        for (i <- 0 until equipment.maxFood if !slots.get(i).active) {
          slots.get(i).activate(equipment.slots(0)(i), equipment.slots(1)(i), equipment, this)
        }
      }
      if (equipment == null) {
        for (i <- 0 until 10 if slots.get(i).active) {
          slots.get(i).deActivate()
          if (slots.get(i).getStack != null) {
            worldObj.dropItemInWorld(slots.get(i).getStack, xCoord, yCoord, zCoord, new Random())
          }
        }
      }
    }
  }
  def manageFuel() {
    if (fuel > 0) {
      fuel -= 1
      sendTileData(0, true, fuel)
    }
    if (((fuel + fuelForCoal) <= maxFeul) && (getStackInSlot(0) != null)) {
      decrStackSize(0, 1)
      fuel += fuelForCoal
    }
  }
  def manageLight() {
    if (active != oldActive) update = 5
    if (update > 0) {
      update -= 1
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    }
  }
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    fuel = tag.getInteger("fuel")
    cookProgress = tag.getIntArray("cookProgress")
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) {
      coals(i)(j) = tag.getFloat("coals" + i + j)
    }
    super[TileEntityWithInventory].readFromNBT(tag)
  }
  def setSlots(slots: ArrayList[SlotCooking]) = {
    this.slots = slots
  }
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 0) fuel = data(0)
    if (id == 1) cookProgress(data(1)) = data(0)
  }
  override def updateEntity() {
    manageCookingEquipment()
    if (!worldObj.isRemote) {
      active = fuel > 0
      manageFuel()
      cookFood()
      manageLight()
      oldActive = active
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("fuel", fuel)
    tag.setIntArray("cookProgress", cookProgress)
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) {
      tag.setFloat("coals" + i + j, coals(i)(j))
    }
    super[TileEntityWithInventory].writeToNBT(tag)
  }
}
