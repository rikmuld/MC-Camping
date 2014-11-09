package com.rikmuld.camping.common.objs.tile

import TileEntityTent._
import scala.collection.JavaConversions._
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.misc.BoundsStructure
import com.rikmuld.camping.misc.BoundsTracker
import com.rikmuld.camping.common.inventory.SlotState
import net.minecraft.entity.player.EntityPlayer.EnumStatus
import net.minecraft.item.ItemStack
import cpw.mods.fml.relauncher.SideOnly
import com.rikmuld.camping.misc.Bounds
import java.util.ArrayList
import net.minecraft.block.Block
import cpw.mods.fml.relauncher.Side
import com.rikmuld.camping.core.Objs
import net.minecraft.init.Blocks
import com.rikmuld.camping.core.Utils._
import java.util.Random
import com.rikmuld.camping.common.network.PacketSender
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.util.ChatComponentText
import com.rikmuld.camping.common.network.PlayerSleepInTent
import net.minecraft.tileentity.TileEntity

object TileEntityTent {
  var bounds: Array[Bounds] = Array(new Bounds(-0.5F, 0, 0, 1.5F, 1.5F, 3), new Bounds(-2, 0, -0.5F, 1, 1.5F, 1.5F), new Bounds(-0.5F, 0, -2, 1.5F, 1.5F, 1), new Bounds(0, 0, -0.5F, 3, 1.5F, 1.5F))
  var LANTERN: Int = 0
  var CHEST: Int = 1
  var BEDS: Int = 2
}

class TileEntityTent extends TileEntityWithRotation with TileEntityWithInventory {
  var slots: Array[Array[SlotState]] = _
  var structures: Array[BoundsStructure] = _
  var tracker: Array[BoundsTracker] = new Array[BoundsTracker](4)
  var isNew: Boolean = true
  var dropped: Boolean = _
  var contendList: Array[Block] = Array(Objs.lantern, Blocks.chest, Objs.sleepingBag)
  var maxContends: Int = 10
  var chestCost: Int = 2
  var bedCost: Int = 5
  var lanternCost: Int = 1
  var chests: Int = _
  var beds: Int = _
  var lanterns: Int = _
  var maxChests: Int = 5
  var maxBeds: Int = 1
  var maxLanterns: Int = 1
  var contends: Int = _
  var time: Int = -1
  var oldTime: Int = _
  var lanternDamage: Int = 0
  var update: Int = _
  var sleepingPlayer: EntityPlayer = _
  var needLightUpdate: Boolean = true
  var lanternUpdateTick: Int = 3
  var slide: Int = _
  var maxSlide: Int = 144
  var chestTracker: Int = _
  var lanternTracker: Int = _
  var color: Int = 15

  def addBed(): Boolean = {
    if (((contends + bedCost) <= maxContends) && (beds < maxBeds)) {
      setContends(beds + 1, BEDS, true, 0)
      return true
    }
    false
  }
  def addChests(): Boolean = {
    if (((contends + chestCost) <= maxContends) && (chests < maxChests)) {
      setContends(chests + 1, CHEST, true, 0)
      return true
    }
    false
  }
  def addContends(stack: ItemStack): Boolean = {
    val id = Block.getBlockFromItem(stack.getItem()) //TODO: CHECK!!! IF THE TILE GIVES AN ERROR THIS MAY BE THE CAUSE, CHANGE SYSTEM IF SO!!!!!
    if (id == contendList(0)) return addLentern(stack)
    if (id == contendList(1)) return addChests()
    if (id == contendList(2)) return addBed()
    false
  }
  def addLentern(stack: ItemStack): Boolean = {
    if (((contends + lanternCost) <= maxContends) && (lanterns < maxLanterns)) {
      time = if (stack.hasTagCompound()) stack.getTagCompound.getInteger("time") else -1
      lanternDamage = if (time > 0) 0 else 1
      sendTileData(3, true, lanternDamage)
      setContends(lanterns + 1, LANTERN, true, 0)
      return true
    }
    false
  }
  def getContends(): ArrayList[ItemStack] = {
    val stacks = new ArrayList[ItemStack]()
    val lanternStack = new ItemStack(contendList(0), lanterns, if (time > 0) 0 else 1)
    if (time > 0) {
      lanternStack.setTagCompound(new NBTTagCompound())
      lanternStack.getTagCompound.setInteger("time", time)
    }
    if (lanterns > 0) stacks.add(lanternStack)
    if (chests > 0) stacks.add(new ItemStack(contendList(1), chests, 0))
    if (beds > 0) stacks.add(new ItemStack(contendList(2), beds, 0))
    stacks
  }
  def getContendsFor(block: Block): ItemStack = {
    if (block == Objs.lantern) {
      val lanternStack = new ItemStack(contendList(0), lanterns, if (time > 0) 0 else 1)
      if (time > 0) {
        lanternStack.setTagCompound(new NBTTagCompound())
        lanternStack.getTagCompound.setInteger("time", time)
      }
      return lanternStack
    }
    if (block == Blocks.chest) return new ItemStack(contendList(1), 1, 0)
    if (block == Objs.sleepingBag) return new ItemStack(contendList(2), 1, 0)
    null
  }
  private def getExcesChestContends(): ArrayList[ItemStack] = {
    val list = new ArrayList[ItemStack]()
    for (i <- chests * 5 * 6 until 150 if getStackInSlot(i + 1) != null) {
      list.add(getStackInSlot(i + 1))
      setInventorySlotContents(i + 1, null)
    }
    list
  }
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = {
    val bb = TileEntity.INFINITE_EXTENT_AABB;
    val bound = TileEntityTent.bounds(rotation)
    AxisAlignedBB.getBoundingBox(bound.xMin, bound.yMin, bound.zMin, bound.xMax, bound.yMax, bound.zMax)
    bb
  }
  override def getSizeInventory(): Int = 151
  def initalize() {
    if (!worldObj.isRemote) {
      structures = Objs.tentStructure
      for (i <- 0 until 4) {
        tracker(i) = new BoundsTracker(xCoord, yCoord, zCoord, bounds(i))
      }
      isNew = false
    }
  }
  def manageSlots() {
    if (slots != null) {
      if (chests > 2) {
        val scaledSlide = slide.getScaledNumber(144, (5 * chests) - 11).toInt
        for (i <- 0 until (5 * chests); j <- 0 until 6) {
          slots(i)(j).setStateX(scaledSlide)
          if ((i < scaledSlide) || (i >= (scaledSlide + 11))) {
            slots(i)(j).disable
          } else {
            slots(i)(j).enable
          }
        }
      } else {
        for (i <- 0 until (5 * chests); j <- 0 until 6) {
          slots(i)(j).setStateX(0)
          slots(i)(j).enable()
        }
      }
    }
  }
  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)
    super[TileEntityWithInventory].readFromNBT(tag)
    contends = tag.getInteger("contends")
    beds = tag.getInteger("beds")
    lanterns = tag.getInteger("lanterns")
    chests = tag.getInteger("chests")
    lanternDamage = tag.getInteger("lanternDamage")
    time = tag.getInteger("time")
    color = tag.getInteger("color")
  }
  def removeAll() {
    setContends(0, 0, true, 2)
    setContends(0, 1, true, 0)
    setContends(0, 2, true, 0)
  }
  def removeBed(): Boolean = {
    if (beds > 0) {
      setContends(beds - 1, BEDS, true, 1)
      worldObj.dropItemInWorld(getContendsFor(Objs.sleepingBag), xCoord, yCoord, zCoord, new Random())
      return true
    }
    false
  }
  def removeChest(): Boolean = {
    if (chests > 0) {
      setContends(chests - 1, CHEST, true, 1)
      worldObj.dropItemInWorld(getContendsFor(Blocks.chest), xCoord, yCoord, zCoord, new Random())
      return true
    }
    false
  }
  def removeLantern(): Boolean = {
    if (lanterns > 0) {
      setContends(lanterns - 1, LANTERN, true, 1)
      worldObj.dropItemInWorld(getContendsFor(Objs.lantern), xCoord, yCoord, zCoord, new Random())
      return true
    }
    false
  }
  def setColor(color: Int) {
    if (!worldObj.isRemote) {
      this.color = color
      sendTileData(6, true, color)
    }
  }
  def setContends(contendNum: Int, contendId: Int, sendData: Boolean, drop: Int) {
    if (drop == 1) worldObj.dropItemInWorld(getContendsFor(contendList(contendId)), xCoord, yCoord, zCoord, new Random())
    if (drop == 2) worldObj.dropItemsInWorld(getContends, xCoord, yCoord, zCoord, new Random())
    if (sendData) sendTileData(1, !worldObj.isRemote, contendNum, contendId, drop)
    if (contendId == LANTERN) lanterns = contendNum
    if (contendId == CHEST) chests = contendNum
    if (contendId == BEDS) beds = contendNum
    contends = (beds * bedCost) + (chests * chestCost) + (lanterns * lanternCost)
    sendTileData(2, !worldObj.isRemote, contends)
    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
    worldObj.markBlockRangeForRenderUpdate(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 1, zCoord + 1)
  }
  override def setRotation(rotation: Int) {
    if (!worldObj.isRemote) {
      if (isNew) initalize()
      else structures(this.rotation).destroyStructure(worldObj, tracker(this.rotation))
      this.rotation = rotation
      sendTileData(0, true, this.rotation)
      structures(this.rotation).createStructure(worldObj, tracker(this.rotation))
    }
  }
  def setSlideState(slideState: Int) {
    slide = slideState
    manageSlots()
    sendTileData(4, false, slideState)
  }
  def setSlots(slots: Array[Array[SlotState]]) = this.slots = slots
  override def setTileData(id: Int, data: Array[Int]) {
    super.setTileData(id, data)
    if (id == 1) setContends(data(0), data(1), false, data(2))
    else if (id == 2) contends = data(0)
    else if (id == 3) lanternDamage = data(0)
    else if (id == 4) {
      slide = data(0)
      manageSlots()
    } else if (id == 5) time = data(0)
    else if (id == 6) color = data(0)
  }
  def sleep(player: EntityPlayer) {
    if (!worldObj.isRemote) {
      if (sleepingPlayer == null) {
        var state: EnumStatus = null
        if (rotation == 0) state = player.sleepInBedAt(xCoord, yCoord, zCoord + 1)
        else if (rotation == 1) state = player.sleepInBedAt(xCoord - 1, yCoord, zCoord)
        else if (rotation == 2) state = player.sleepInBedAt(xCoord, yCoord, zCoord - 1)
        else if (rotation == 3) state = player.sleepInBedAt(xCoord + 1, yCoord, zCoord)
        if (state != EnumStatus.OK) {
          if (state == EnumStatus.NOT_POSSIBLE_NOW) {
            player.addChatMessage(new ChatComponentTranslation("tile.bed.noSleep", new java.lang.Object))
          } else if (state == EnumStatus.NOT_SAFE) {
            player.addChatMessage(new ChatComponentTranslation("tile.bed.noSafe", new java.lang.Object))
          }
        }
      } else {
        player.addChatMessage(new ChatComponentText("This tent is occupied!"))
      }
    } else {
      PacketSender.toServer(new PlayerSleepInTent(xCoord, yCoord, zCoord))
    }
  }
  override def updateEntity() {
    if (!worldObj.isRemote) {
      oldTime = time
      if (chestTracker != chests) {
        chestTracker = chests
        worldObj.dropItemsInWorld(getExcesChestContends, xCoord, yCoord, zCoord, new Random())
      }
      if (lanternTracker != lanterns) {
        lanternTracker = lanterns
        if (lanterns == 0) {
          if (getStackInSlot(0) != null) {
            worldObj.dropItemInWorld(getStackInSlot(0), xCoord, yCoord, zCoord, new Random())
          }
          setInventorySlotContents(0, null)
        }
      }
      if (needLightUpdate) {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
        worldObj.markBlockRangeForRenderUpdate(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 1, zCoord + 1)
        if (lanternUpdateTick == 0) needLightUpdate = false
        else if (lanternUpdateTick > 0) lanternUpdateTick -= 1
      }
      if (isNew) initalize()
      update += 1
      if ((update > 10) && (time > 0)) {
        time -= 1
        update = 0
      }
      if (time == 0) {
        time = -1
        lanternDamage = 1
        sendTileData(3, true, lanternDamage)
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
        worldObj.markBlockRangeForRenderUpdate(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 1, zCoord + 1)
      }
      if ((sleepingPlayer != null) && !sleepingPlayer.isPlayerSleeping) {
        sleepingPlayer = null
      }
      if ((time <= 0) && (getStackInSlot(0) != null)) {
        decrStackSize(0, 1)
        time = 1500
        lanternDamage = 0
        sendTileData(3, true, lanternDamage)
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
        worldObj.markBlockRangeForRenderUpdate(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 1, yCoord + 1, zCoord + 1)
      }
      if (time != oldTime) {
        sendTileData(5, true, time)
      }
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("contends", contends)
    tag.setInteger("beds", beds)
    tag.setInteger("lanterns", lanterns)
    tag.setInteger("chests", chests)
    tag.setInteger("lanternDamage", lanternDamage)
    tag.setInteger("time", time)
    tag.setInteger("color", color)
    super[TileEntityWithInventory].writeToNBT(tag)
  }
}