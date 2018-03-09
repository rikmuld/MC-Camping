package com.rikmuld.camping.tileentity

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Library.AdvancementInfo
import com.rikmuld.camping.entity.Mountable
import com.rikmuld.camping.inventory.SlotCooking
import com.rikmuld.camping.misc.{CookingEquipment, ItemsData}
import com.rikmuld.camping.objs.Definitions.CampfireCook._
import com.rikmuld.camping.objs.Definitions.CampfireWood._
import com.rikmuld.camping.objs.Definitions.Parts
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.tileentity.TileCampfire._
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.corerm.objs.blocks.BlockSimple
import com.rikmuld.corerm.tileentity.{TileEntityInventory, TileEntitySimple, TileEntityTicker}
import com.rikmuld.corerm.utils.WorldUtils
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
import scala.collection.mutable.ArrayBuffer

object TileCampfire {
  lazy val effects: Seq[PotionEffect] =
    for (i <- Seq(20, 5, 19, 11, 16, 9, 1, 18, 15, 10, 17, 3, 13, 10, 12))
      yield new PotionEffect(Potion.getPotionById(i), 400, 0)

  def isAsh(stack: ItemStack): Boolean =
    stack.getItem == ObjRegistry.parts &&
      stack.getItemDamage == Parts.ASH
}

class TileCampfireWoodOn extends TileEntitySimple with TileEntityTicker with Roaster {
  registerTicker(updateEffects, 100)

  def getEntityBounds: AxisAlignedBB =
    getBlock.getBoundingBox(world.getBlockState(pos), world, pos).offset(pos).grow(8)

  def updateEffects(): Unit = {
    val light = getBlock.getInt(world, pos, STATE_LIGHT)

    if (!world.isRemote && light < 15) {
      world.getEntitiesWithinAABB(classOf[EntityLivingBase], getEntityBounds).foreach(entity =>
        entity.addPotionEffect(new PotionEffect(effects(light)))
      )
    }
  }

  def getBlock: BlockSimple =
    ObjRegistry.campfireWoodOn

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean =
    oldState.getBlock != newState.getBlock

  override def roastTime(item: ItemStack): Int =
    350
}


class TileCampfireWoodOff extends TileEntitySimple with TileEntityTicker {
  registerTicker(updateLight, 3)

  def updateLight() {
    if (!world.isRemote) {
      val light = getBlock.getInt(world, pos, STATE_LIGHT)

      if (light > 0)
        getBlock.setState(world, pos, STATE_LIGHT, Math.max(0, light - 1))
    }
  }

  def getBlock: BlockSimple =
    ObjRegistry.campfireWood

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean =
    oldState.getBlock != newState.getBlock
}

trait Roaster {
  def canRoast(item: ItemStack): Boolean =
    item.getItem == ObjRegistry.parts &&
      item.getItemDamage == Parts.MARSHMALLOW_STICK

  protected def roastResult(item:ItemStack): ItemStack =
    new ItemStack(ObjRegistry.marshmallow)

  def roastTime(item: ItemStack): Int =
    150

  def roastSpeed(item: ItemStack): Float =
    1

  def roast(player: EntityPlayer, item: ItemStack): Option[ItemStack] =
    if(!canRoast(item)) None
    else {
      val result = roastResult(item)
      val roastTrigger =
        !player.world.isRemote &&
          Option(player.getRidingEntity).isDefined &&
          player.getRidingEntity.isInstanceOf[Mountable]

      if (roastTrigger)
        TriggerHelper.trigger(AdvancementInfo.FOOD_ROASTED, player, (item, result))

      Some(result)
    }
}

class TileCampfireCook extends TileEntityInventory with Roaster with ITickable {

  final val MAX_FUEL =
    config.campfireMaxFuel

  final val COAL_FUEL =
    config.campfireCoalFuel

  private var fuel: Int =
    0

  private var cookProgress: Array[Int] =
    new Array[Int](10)

  private var equipment: Option[CookingEquipment] =
    None

  //TODO don't do slots stuff in tile, but container
  private var slots: Option[Seq[SlotCooking]] =
    None

  private val guiPlayers: ArrayBuffer[EntityPlayer] =
    new ArrayBuffer()

  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox: AxisAlignedBB =
    new AxisAlignedBB(pos.getX, pos.getY, pos.getZ, pos.getX + 1, pos.getY + 1, pos.getZ + 1)

  override def shouldRefresh(world: World, pos: BlockPos, old: IBlockState, nw: IBlockState): Boolean =
    old.getBlock != nw.getBlock

  override def readFromNBT(tag: NBTTagCompound): Unit = {
    super.readFromNBT(tag)

    fuel = tag.getInteger("fuel")
    cookProgress = tag.getIntArray("cookProgress")
    equipment = CookingEquipment.getEquipment(getStackInSlot(1))
  }

  override def writeToNBT(tag: NBTTagCompound): NBTTagCompound = {
    tag.setInteger("fuel", fuel)
    tag.setIntArray("cookProgress", cookProgress)

    super.writeToNBT(tag)
  }

  override def canRoast(item: ItemStack): Boolean =
    fuel > 0 && super.canRoast(item)

  override def roastSpeed(item: ItemStack): Float =
    0.75f + (getCoalPieces / 12f)

  override def getSizeInventory: Int =
    12

  override def getName: String =
    "campfire_cooking"

  override def closeInventory(player: EntityPlayer): Unit = {
    slots = None
    guiPlayers -= player
  }

  def getCoalPieces(fuel: Int): Int =
    Math.ceil(fuel / COAL_FUEL.toFloat).toInt

  def getCoalPieces: Int =
    getCoalPieces(fuel)

  def getEquipment: Option[CookingEquipment] =
    equipment

  def getScaledFuel(maxPixels: Int): Float =
    (fuel.toFloat / MAX_FUEL) * maxPixels

  def getScaledCookProgress(maxPixels: Int, slot: Int): Float =
    equipment.fold(0f)(equip =>
      (cookProgress(slot).toFloat + 1) / equip.getCookTime
    ) * maxPixels

  def getCookProcess(slot: Int): Int =
    cookProgress(slot)

  def getFuel: Int =
    fuel

  @SideOnly(Side.CLIENT)
  def setFuel(fuel: Int): Unit =
    this.fuel = fuel

  @SideOnly(Side.CLIENT)
  def setCookProcess(slot: Int, data: Int): Unit =
    cookProgress(slot) = data

  def setSlots(newSlots: Seq[SlotCooking]): Unit =
    slots = Some(newSlots)

  override def setTileData(id: Int, data: Seq[Int]): Unit = id match {
    case 0 =>
      fuel = data.head
    case 1 =>
      for(i <- data.indices)
        cookProgress(i) = data(i)
  }

  private def cookFood(): Unit = {
    equipment.foreach(eq => {
      for (i <- 0 until eq.getMaxCookingSlot)
        cookFood(i, getStackInSlot(i + 2), eq)

      guiPlayers.foreach(player =>
        sendTileDataTo(1, player, cookProgress.take(eq.getMaxCookingSlot): _*)
      )
    })
  }

  private def cookFood(i: Int, food: ItemStack, equipment: CookingEquipment): Unit =
    if (fuel == 0 || food.isEmpty || isAsh(food)) cookProgress(i) = 0
    else {
      if (cookProgress(i) >= equipment.getCookTime)
        doCookFood(i, food, equipment)
      else
        cookProgress(i) += 1
    }

  private def doCookFood(i: Int, food: ItemStack, equipment: CookingEquipment): Unit = {
    val result = equipment.getResult(food).getOrElse(
      new ItemStack(ObjRegistry.parts, 1, Parts.ASH)
    )

    cookProgress(i) = 0

    setInventorySlotContents(i + 2, result)
    PacketSender.sendToClient(new ItemsData(i + 2, pos.getX, pos.getY, pos.getZ, result.copy()))
  }

  def initializeSlot(index: Int, slot: SlotCooking): Unit = {
    if (slot.active && !slot.getStack.isEmpty) {
      WorldUtils.dropItemInWorld(world, slot.getStack, pos)
      setInventorySlotContents(index + 2, ItemStack.EMPTY)
    }

    if (equipment.isEmpty)
      slot.deActivate()
    else if (index < equipment.get.getMaxCookingSlot) {
      val position = equipment.get.getSlotPosition(index)
      slot.activate(position._1, position._2, equipment.get, this)
    }
  }

  override def onChange(slot: Int): Unit =
    if (slot == 1) {
      (equipment, CookingEquipment.getEquipment(getStackInSlot(1))) match {
        case (None, Some(nw)) =>
          equipment = Some(nw)
          initializeSlots()
        case (Some(old), None) =>
          equipment = None
          initializeSlots()
        case (Some(old), Some(nw)) if old != nw =>
          equipment = Some(nw)
          initializeSlots()
        case _ =>
      }
    }

  override def openInventory(player: EntityPlayer): Unit = {
    initializeSlots()
    guiPlayers.add(player)
  }

  def initializeSlots(): Unit =
    slots.foreach(slots =>
      for (i <- 0 until 10)
        initializeSlot(i, slots(i))
    )

  def addFuel(): Unit = {
    if (fuel == 0)
      getBlock.setState(world, pos, STATE_ON, true)

    decrStackSize(0, 1)

    fuel += COAL_FUEL
  }

  def removeFuel(): Unit = {
    fuel -= 1

    if (fuel == 0 && !hasCoal)
      getBlock.setState(world, pos, STATE_ON, false)
  }

  def fuelTick(): Unit = {
    val oldFuel = fuel

    if (fuel > 0)
      removeFuel()

    if (hasCoal && fuel + COAL_FUEL <= MAX_FUEL)
      addFuel()

    if (fuel != oldFuel)
      fuelChanged(oldFuel, fuel)
  }

  def fuelChanged(old: Int, nw: Int): Unit =
    if (getCoalPieces != getCoalPieces(old))
      sendTileData(0, true, fuel)
    else guiPlayers.foreach(player =>
      sendTileDataTo(0, player, fuel)
    )

  def hasCoal: Boolean =
    !getStackInSlot(0).isEmpty

  def isOn: Boolean =
    fuel > 0

  def getBlock: BlockSimple =
    ObjRegistry.campfireCook

  override def update(): Unit =
    if (!world.isRemote) {
      fuelTick()
      cookFood()
    }
}