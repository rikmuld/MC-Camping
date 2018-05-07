package com.rikmuld.camping.features.blocks.campfire.cook

import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Definitions.CampfireCook._
import com.rikmuld.camping.Definitions.Parts
import com.rikmuld.camping.features.blocks.campfire.Roaster
import com.rikmuld.camping.features.blocks.campfire.cook.equipment.CookingEquipment
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.corerm.network.packets.PacketItemData
import com.rikmuld.corerm.tileentity.TileEntityInventory
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

object TileEntityCampfireCook {
  def isAsh(stack: ItemStack): Boolean =
    stack.getItem == CampingMod.OBJ.parts && stack.getItemDamage == Parts.ASH
}

class TileEntityCampfireCook extends TileEntityInventory with Roaster with ITickable {

  final val MAX_FUEL =
    CONFIG.campfireMaxFuel

  final val COAL_FUEL =
    CONFIG.campfireCoalFuel

  private var fuel: Int =
    0

  private var cookProgress: Array[Int] =
    new Array[Int](10)

  private var equipment: Option[CookingEquipment] =
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

  override def closeInventory(player: EntityPlayer): Unit =
    guiPlayers -= player

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
    if (fuel == 0 || food.isEmpty || TileEntityCampfireCook.isAsh(food)) cookProgress(i) = 0
    else {
      if (cookProgress(i) >= equipment.getCookTime)
        doCookFood(i, food, equipment)
      else
        cookProgress(i) += 1
    }

  private def doCookFood(i: Int, food: ItemStack, equipment: CookingEquipment): Unit = {
    val result = equipment.getResult(food).getOrElse(
      new ItemStack(CampingMod.OBJ.parts, 1, Parts.ASH)
    )

    cookProgress(i) = 0

    setInventorySlotContents(i + 2, result)
    PacketSender.sendToClient(new PacketItemData(i + 2, pos.getX, pos.getY, pos.getZ, result.copy()))
  }

  override def onChange(slot: Int): Unit =
    if (slot == 1) {
      (equipment, CookingEquipment.getEquipment(getStackInSlot(1))) match {
        case (None, Some(nw)) =>
          equipment = Some(nw)
        case (Some(old), None) =>
          equipment = None
        case (Some(old), Some(nw)) if old != nw =>
          equipment = Some(nw)
        case _ =>
      }
    }

  override def openInventory(player: EntityPlayer): Unit =
    guiPlayers.add(player)

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

  override def update(): Unit =
    if (!world.isRemote) {
      fuelTick()
      cookFood()
    }
}