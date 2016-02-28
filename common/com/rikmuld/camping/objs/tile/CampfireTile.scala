package com.rikmuld.camping.objs.tile

import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.camping.CampingMod._
import com.rikmuld.corerm.objs.WithTileInventory
import net.minecraft.entity.EntityLivingBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.potion.PotionEffect
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random
import java.util.ArrayList
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.corerm.misc.WorldBlock._
import com.rikmuld.corerm.CoreUtils._
import scala.collection.JavaConversions._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.camping.inventory.SlotCooking
import com.rikmuld.corerm.CoreUtils._
import net.minecraft.item.ItemStack
import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.objs.ItemDefinitions
import com.rikmuld.camping.objs.misc.ItemsData
import net.minecraft.server.gui.IUpdatePlayerListBox
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.Lib.NBTInfo

class TileCampfire extends RMTile with IUpdatePlayerListBox {
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

  def renderCoal = false
  private def colorFlame(color: Int) {
    this.color = color
    if (!worldObj.isRemote) sendTileData(0, true, color)
  }
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = AxisAlignedBB.fromBounds(bd.x, bd.y, bd.z, bd.x + 1, bd.y + 1, bd.z + 1)
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    color = tag.getInteger("color")
    time = tag.getInteger("time")
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) {
      coals(i)(j) = tag.getFloat("coals" + i + j)
    }
  }
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 0) colorFlame(data(0))
  }
  def addDye(stack:ItemStack):Boolean = {
    if ((stack != null) && ((time == 0)) || color != stack.getItemDamage ) {
      colorFlame(stack.getItemDamage)
      time = 6000
      true
    } else false
  }
  override def update() {
    if (!worldObj.isRemote) {
      if (active > 0) {
        active -= 1
        bd.update
        bd.updateRender
      }
      oldTime = time
      if (time != 0) time -= 1
      if ((color != 16) && (time == 0)) colorFlame(16)
      if ((time > 0) && ((time % 120) == 0)) {
        val entitys = worldObj.getEntitiesWithinAABB(classOf[EntityLivingBase], AxisAlignedBB.fromBounds(bd.x - 8, bd.y - 8, bd.z - 8, bd.x + 8, bd.y + 8, bd.z + 8)).asInstanceOf[ArrayList[EntityLivingBase]]
        for (entity <- entitys) entity.addPotionEffect(new PotionEffect(effectsOrderd(color)))
      }
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("color", color)
    tag.setInteger("time", time)
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) tag.setFloat("coals" + i + j, coals(i)(j))
  }
}

class TileCampfireWood extends TileCampfire {
  private var fuel = config.maxWoodFuel
  private var oldLight = 16
  
  override def update() {
    if (!worldObj.isRemote) {
      fuel-=1
      sendTileData(1, true, fuel)
      if(fuel<=0){
        break()
      }
    }
    val newLight = Math.ceil(fuel/(config.maxWoodFuel/16.0)).toInt
    if(newLight != oldLight){
      updateLight
      oldLight = newLight
    }
  }
  def updateLight {
    getWorld.theProfiler.startSection("checkLight")
    getWorld.checkLight(pos)
    getWorld.theProfiler.endSection
  }
  def break(){
    worldObj.dropItemInWorld(new ItemStack(Objs.parts, rand.nextInt(3), ItemDefinitions.Parts.ASH), pos.getX, pos.getY, pos.getZ, rand)
    worldObj.setBlockToAir(pos)
  }
  override def renderCoal = false
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("fuel", fuel)
  }
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    fuel = tag.getInteger("fuel")
  }
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 1) fuel = data(0)
    else super.setTileData(id, data)
  }
  def getFuel():Int = oldLight
}

class TileCampfireCook extends RMTile with WithTileInventory with IUpdatePlayerListBox {
  var maxFeul: Int = config.campfireMaxFuel
  var fuelForCoal: Int = config.campfireCoalFuel
  var fuel: Int = _
  var coals: Array[Array[Float]] = Array.ofDim[Float](3, 20)
  var rand: Random = new Random()
  var cookProgress: Array[Int] = new Array[Int](10)
  var oldCookProgress: Array[Int] = new Array[Int](10)
  var equipment: CookingEquipment = _
  var slots: ArrayList[SlotCooking] = _
  private var active: Boolean = _
  private var oldActive: Boolean = _
  private var updateNum: Int = _
  var lastPlayer:EntityPlayer = _

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
            if (equipment.getCookedFood(getStackInSlot(i + 2)) != null) setInventorySlotContents(i + 2, equipment.getCookedFood(getStackInSlot(i + 2)).copy())
            else setInventorySlotContents(i + 2, new ItemStack(Objs.parts, 1, ItemDefinitions.Parts.ASH))
          } else setInventorySlotContents(i + 2, new ItemStack(Objs.parts, 1, ItemDefinitions.Parts.ASH))
          PacketSender.toClient(new ItemsData(i + 2, bd.x, bd.y, bd.z, getStackInSlot(i + 2)))
        }
        if (fuel > 0) {
          if ((getStackInSlot(i + 2) != null) &&
            (!(getStackInSlot(i + 2).getItem == Objs.parts) ||
              getStackInSlot(i + 2).getItemDamage != ItemDefinitions.Parts.ASH)) {
            cookProgress(i) += 1
          }
        } else if (cookProgress(i) > 0) cookProgress(i) = 0
        if ((getStackInSlot(i + 2) == null) && (cookProgress(i) > 0)) cookProgress(i) = 0
        if (oldCookProgress(i) != cookProgress(i)) sendTileData(1, true, cookProgress(i), i)
      }
    }
  }
  def getCoalPieces(): Int = if (fuel > 0) (if (((fuel / fuelForCoal) + 1) <= 20) (fuel / fuelForCoal) + 1 else 20) else 0
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = AxisAlignedBB.fromBounds(bd.x, bd.y, bd.z, bd.x + 1, bd.y + 1, bd.z + 1)
  def getScaledCoal(maxPixels: Int): Float = (fuel.toFloat / maxFeul.toFloat) * maxPixels
  def getScaledcookProgress(maxPixels: Int, foodNum: Int): Float = ((cookProgress(foodNum).toFloat + 1) / equipment.cookTime) * maxPixels
  override def getSizeInventory(): Int = 12
  def manageCookingEquipment() {
    if ((equipment == null) && (getStackInSlot(1) != null)) equipment = CookingEquipment.getCooking(getStackInSlot(1))
    else if ((equipment != null) && (getStackInSlot(1) == null)) equipment = null
    if (slots != null) {
      if (equipment != null) {
        for (i <- 0 until equipment.maxFood if !slots.get(i).active) slots.get(i).activate(equipment.slots(0)(i), equipment.slots(1)(i), equipment, this)
      }
      if (equipment == null) {
        for (i <- 0 until 10 if slots.get(i).active) {
          slots.get(i).deActivate()
          if (slots.get(i).getStack != null) bd.world.dropItemInWorld(slots.get(i).getStack, bd.x, bd.y, bd.z, new Random())
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
    if (active != oldActive) updateNum = 5
    if (updateNum > 0) {
      updateNum -= 1
      bd.update
      bd.updateRender
      updateLight
    }
  }
  def updateLight {
    getWorld.theProfiler.startSection("checkLight")
    getWorld.checkLight(pos)
    getWorld.theProfiler.endSection
    
    if(!worldObj.isRemote)sendTileData(2, true, 0)
  }
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    fuel = tag.getInteger("fuel")
    cookProgress = tag.getIntArray("cookProgress")
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) coals(i)(j) = tag.getFloat("coals" + i + j)
    super[WithTileInventory].readFromNBT(tag)
  }
  def setSlots(slots: ArrayList[SlotCooking]) = this.slots = slots
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 0) fuel = data(0)
    if (id == 1) cookProgress(data(1)) = data(0)
    if (id == 2) updateLight
  }
  override def update(){
    var equipOld = equipment
    manageCookingEquipment()
    if (!worldObj.isRemote) {
      if(equipOld != equipment)Option(lastPlayer).map(checkCampfireAch)
      active = fuel > 0
      manageFuel()
      cookFood()
      manageLight()
      oldActive = active
    }
  }
  def checkCampfireAch(player:EntityPlayer){
    var data = player.getEntityData
    if(!data.hasKey(NBTInfo.ACHIEVEMENTS))data.setTag(NBTInfo.ACHIEVEMENTS, new NBTTagCompound())
    data = data.getTag(NBTInfo.ACHIEVEMENTS).asInstanceOf[NBTTagCompound]
    var campfires = if(data.hasKey("camp_make")) data.getIntArray("camp_make") else Array(0, 0, 0)
    if(equipment == Objs.spit)campfires(0) = 1
    else if(equipment == Objs.grill)campfires(1) = 1
    else if(equipment == Objs.pan)campfires(2) = 1
    if(campfires(0) == 1 && campfires(1) == 1 && campfires(2) == 1) player.triggerAchievement(Objs.achCampfire)
    data.setIntArray("camp_make", campfires)
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("fuel", fuel)
    tag.setIntArray("cookProgress", cookProgress)
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) tag.setFloat("coals" + i + j, coals(i)(j))
    super[WithTileInventory].writeToNBT(tag)
  }
}