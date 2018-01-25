package com.rikmuld.camping.objs.tile

import java.util.{ArrayList, Random}

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib.NBTInfo
import com.rikmuld.camping.inventory.SlotCooking
import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.objs.{ItemDefinitions, Objs}
import com.rikmuld.camping.objs.block.{CampfireCook, CampfireWood}
import com.rikmuld.camping.objs.misc.ItemsData
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.corerm.tileentity.{RMTile, WithTileInventory}
import com.rikmuld.corerm.utils.CoreUtils._
import com.rikmuld.corerm.utils.WorldBlock._
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.{Potion, PotionEffect}
import net.minecraft.util.ITickable
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

class TileCampfire extends RMTile with ITickable {
  var color: Int = 16
  var oldTime: Int = _
  var time: Int = _
  var active: Int = 4
  var rand: Random = new Random()
  var coals: Array[Array[Float]] = Array.ofDim[Float](3, 20)
  var effectsOrderd: Array[PotionEffect] = new Array[PotionEffect](16)
  var effectsRaw: Array[PotionEffect] = new Array[PotionEffect](23)

  for (i <- 0 until 23) effectsRaw(i) = new PotionEffect(Potion.getPotionById(i), 400, 0)

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
    if (!world.isRemote) sendTileData(0, true, color)
  }
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB =  new AxisAlignedBB(bd.x, bd.y, bd.z, bd.x + 1, bd.y + 1, bd.z + 1)
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
    if (!stack.isEmpty && ((time == 0)) || color != stack.getItemDamage ) {
      colorFlame(stack.getItemDamage)
      time = 3000
      true
    } else false
  }
  override def update() {
    if (!world.isRemote) {
      if (active > 0) {
        active -= 1
        bd.update
        bd.updateRender
      }
      oldTime = time
      if (time != 0) time -= 1
      if ((color != 16) && (time == 0)) colorFlame(16)
      if ((time > 0) && ((time % 120) == 0)) {
        val entitys = world.getEntitiesWithinAABB(classOf[EntityLivingBase], new AxisAlignedBB(bd.x - 8, bd.y - 8, bd.z - 8, bd.x + 8, bd.y + 8, bd.z + 8)).asInstanceOf[ArrayList[EntityLivingBase]]
        for (entity <- entitys) entity.addPotionEffect(new PotionEffect(effectsOrderd(color)))
      }
    }
  }
  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound = {
    tag.setInteger("color", color)
    tag.setInteger("time", time)
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) tag.setFloat("coals" + i + j, coals(i)(j))
    super.writeToNBT(tag)    
  }
}

class TileCampfireWood extends TileCampfire with Roaster {
  private var on = false
  val maxFeul = config.maxWoodFuel
  private var fuel = 0
  private var lightState = 0
  private var oldLightState = 0
  private var lid = 0
  
  override def canRoast(item:ItemStack):Boolean = {
    this.fuel > 0 && 
    this.on &&
    (item.getItem, item.getItemDamage) == (Objs.parts, ItemDefinitions.Parts.MARSHMALLOWSTICK)
  }
  override def roastResult(item:ItemStack) = new ItemStack(Objs.marshmallow)
  override def roastSpeed(item:ItemStack) = ((Math.log10((100*fuel)/maxFeul + 1) + 0.25)/2).toFloat
  override def roastTime(item:ItemStack):Int = 350

  override def update() {
    if (!world.isRemote) {
      oldLightState = lightState
      
      if(on){
        fuel-=1
        sendTileData(1, true, fuel)
        if(fuel<=0){
          break()
        }
      } else {
        if(lid>0)lid -= 1
        if(lid>25){
          setOn()
        }
        this.sendTileData(2, true, lid)
      }
      
      lightState = getFuel()
      if(lightState != oldLightState){
        if(bd.block == Objs.campfireWood) Objs.campfireWood.asInstanceOf[CampfireWood].setLight(getWorld, pos, lightState)
      }
    }
    
    super.update
  }
  override def shouldRefresh(world:World, pos:BlockPos, oldState:IBlockState, newState:IBlockState) = oldState.getBlock != newState.getBlock
  def isOn() = on
  def tryLid() = lid += 5
  def getLid() = lid
  def setOn(){
    this.on = true
    this.fuel = maxFeul
    this.sendTileData(4, !world.isRemote, 1)
  }
  def break(){
    world.dropItemInWorld(new ItemStack(Objs.parts, rand.nextInt(3), ItemDefinitions.Parts.ASH), pos.getX, pos.getY, pos.getZ, rand)
    world.setBlockToAir(pos)
  }
  override def renderCoal = false
  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound= {
    tag.setInteger("fuel", fuel)
    tag.setBoolean("on", on)
    super.writeToNBT(tag)
  }
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    fuel = tag.getInteger("fuel")
    on = tag.getBoolean("on")
  }
  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 1) fuel = data(0)
    else if(id == 2)lid = data(0)
    else if (id == 4) {
      on = true
      this.fuel = maxFeul
    }
    else super.setTileData(id, data)
  }
  def getFuel():Int = if(on) Math.min(15, Math.ceil(fuel/(maxFeul/80.0)).toInt) else lid/2
}

abstract trait Roaster {
  def canRoast(item:ItemStack):Boolean
  def roastTime(item:ItemStack):Int = 150
  def roastSpeed(item:ItemStack):Float = 1
  def roastResult(item:ItemStack):ItemStack
}

class TileCampfireCook extends RMTile with WithTileInventory with ITickable with Roaster {
  var maxFeul: Int = config.campfireMaxFuel
  var fuelForCoal: Int = config.campfireCoalFuel
  var fuel: Int = _
  var oldFuel: Int = _
  var coals: Array[Array[Float]] = Array.ofDim[Float](3, 20)
  var rand: Random = new Random()
  var cookProgress: Array[Int] = new Array[Int](10)
  var oldCookProgress: Array[Int] = new Array[Int](10)
  var equipment: CookingEquipment = _
  var slots: Seq[SlotCooking] = _
  var lastPlayer:EntityPlayer = _
  
  for (i <- 0 until 20) {
    coals(0)(i) = rand.nextFloat() / 5F
    coals(1)(i) = rand.nextFloat() / 5F
    coals(2)(i) = rand.nextFloat() * 360
  }

  override def canRoast(item:ItemStack):Boolean = {
    this.fuel > 0 && 
    (item.getItem, item.getItemDamage) == (Objs.parts, ItemDefinitions.Parts.MARSHMALLOWSTICK)
  }
  override def roastResult(item:ItemStack) = new ItemStack(Objs.marshmallow)
  override def roastSpeed(item:ItemStack) = ((Math.log10((100*fuel)/maxFeul + 1) + 0.25)/2).toFloat
  override def roastTime(item:ItemStack):Int = 150

  def getEquipment: Option[CookingEquipment] =
    Option(equipment)

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
          if (!getStackInSlot(i + 2).isEmpty &&
            (!(getStackInSlot(i + 2).getItem == Objs.parts) ||
              getStackInSlot(i + 2).getItemDamage != ItemDefinitions.Parts.ASH)) {
            cookProgress(i) += 1
          }
        } else if (cookProgress(i) > 0) cookProgress(i) = 0
        if (getStackInSlot(i + 2).isEmpty && (cookProgress(i) > 0)) cookProgress(i) = 0
        if (oldCookProgress(i) != cookProgress(i)) sendTileData(1, true, cookProgress(i), i)
      }
    }
  }
  def getCoalPieces(): Int = if (fuel > 0) (if (((fuel / fuelForCoal) + 1) <= 20) (fuel / fuelForCoal) + 1 else 20) else 0
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = new AxisAlignedBB(bd.x, bd.y, bd.z, bd.x + 1, bd.y + 1, bd.z + 1)
  def getScaledCoal(maxPixels: Int): Float = (fuel.toFloat / maxFeul.toFloat) * maxPixels
  def getScaledcookProgress(maxPixels: Int, foodNum: Int): Float = ((cookProgress(foodNum).toFloat + 1) / equipment.cookTime) * maxPixels
  override def getSizeInventory(): Int = 12
  def manageCookingEquipment() {
    var flag = false
    if ((equipment == null) && (!getStackInSlot(1).isEmpty)) equipment = CookingEquipment.getCooking(getStackInSlot(1))
    else if (equipment != null){
      if(getStackInSlot(1).isEmpty){
        equipment = null
      } else {
        val newEquip = CookingEquipment.getCooking(getStackInSlot(1))
        if(newEquip != equipment){
          flag = true
          equipment = newEquip
        }
      }
    }

    if (slots != null) {
      if (equipment == null || flag) {
        for (i <- 0 until 10 if slots.get(i).active) {
          slots.get(i).deActivate()
          if (!slots.get(i).getStack.isEmpty) bd.world.dropItemInWorld(slots.get(i).getStack, bd.x, bd.y, bd.z, new Random())
        }
      }
      if (equipment != null) {
        for (i <- 0 until equipment.maxFood if !slots.get(i).active) slots.get(i).activate(equipment.slots(0)(i), equipment.slots(1)(i), equipment, this)
      }
    }
  }
  def manageFuel() {
    if (fuel > 0) {
      fuel -= 1
      sendTileData(0, true, fuel)
    }
    if (((fuel + fuelForCoal) <= maxFeul) && !getStackInSlot(0).isEmpty) {
      decrStackSize(0, 1)
      fuel += fuelForCoal
    }
  }
  override def readFromNBT(tag: NBTTagCompound) {    
    super[RMTile].readFromNBT(tag)
    super[WithTileInventory].readFromNBT(tag)
        
    fuel = tag.getInteger("fuel")
    cookProgress = tag.getIntArray("cookProgress")
    
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) coals(i)(j) = tag.getFloat("coals" + i + j)
  }
  def setSlots(newSlots: Seq[SlotCooking]) =
    slots = newSlots

  override def setTileData(id: Int, data: Array[Int]) {
    if (id == 0) fuel = data(0)
    if (id == 1) cookProgress(data(1)) = data(0)
  }
  override def shouldRefresh(world:World, pos:BlockPos, oldState:IBlockState, newState:IBlockState) = oldState.getBlock != newState.getBlock
  override def update(){
    var equipOld = equipment
    manageCookingEquipment()
    if (!world.isRemote) {
      if(equipOld != equipment)Option(lastPlayer).map(checkCampfireAch)
      oldFuel = fuel
      manageFuel()
      cookFood()
      if(fuel != oldFuel){
        if(fuel == 0)Objs.campfireCook.asInstanceOf[CampfireCook].setOn(getWorld, pos, false)
        else if(oldFuel == 0)Objs.campfireCook.asInstanceOf[CampfireCook].setOn(getWorld, pos, true)
      }
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
    if(campfires(0) == 1 && campfires(1) == 1 && campfires(2) == 1) player.addStat(Objs.achCampfire)
    data.setIntArray("camp_make", campfires)
  }
  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound = {
    tag.setInteger("fuel", fuel)
    tag.setIntArray("cookProgress", cookProgress)
    for (i <- 0 until coals.length; j <- 0 until coals(i).length) tag.setFloat("coals" + i + j, coals(i)(j))
    super[WithTileInventory].writeToNBT(tag)
    super[RMTile].writeToNBT(tag)    
  }
}