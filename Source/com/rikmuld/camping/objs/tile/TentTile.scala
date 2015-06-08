package com.rikmuld.camping.objs.tile

import com.rikmuld.corerm.objs.WithTileInventory
import com.rikmuld.corerm.misc.WorldBlock._
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.camping.objs.Objs
import net.minecraft.entity.player.EntityPlayer
import com.rikmuld.camping.inventory.SlotState
import net.minecraft.init.Blocks
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import java.util.ArrayList
import net.minecraft.nbt.NBTTagCompound
import com.rikmuld.camping.objs.tile.TileEntityTent._
import net.minecraft.util.ChatComponentText
import com.rikmuld.corerm.network.PacketSender
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.entity.player.EntityPlayer.EnumStatus
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.server.gui.IUpdatePlayerListBox
import com.rikmuld.camping.objs.block.Tent
import net.minecraft.client.renderer.EnumFaceDirection
import net.minecraft.util.EnumFacing
import com.rikmuld.corerm.bounds.BoundsTracker
import com.rikmuld.corerm.bounds.BoundsStructure
import com.rikmuld.corerm.bounds.Bounds
import com.rikmuld.corerm.bounds.IBoundsBlock
import com.rikmuld.camping.objs.misc.PlayerSleepInTent
import com.rikmuld.camping.objs.block.TentBounds

object TileEntityTent {
  var bounds: Array[Bounds] = Array(new Bounds(-0.5F, 0, 0, 1.5F, 1.5F, 3), new Bounds(-2, 0, -0.5F, 1, 1.5F, 1.5F), new Bounds(-0.5F, 0, -2, 1.5F, 1.5F, 1), new Bounds(0, 0, -0.5F, 3, 1.5F, 1.5F))
  var LANTERN: Int = 0
  var CHEST: Int = 1
  var BEDS: Int = 2
}

class TileTent extends RMTile with WithTileInventory with IUpdatePlayerListBox {
  var slots: Array[Array[SlotState]] = _
  var structures: Array[BoundsStructure] = _
  var tracker = new Array[BoundsTracker](4)
  var isNew = true
  var dropped = false
  var contendList = Array(Objs.lantern, Blocks.chest, Objs.sleepingBag)
  var maxContends = 10
  var chestCost = 2
  var bedCost = 5
  var lanternCost = 1
  var chests: Int = _
  var beds: Int = _
  var lanterns: Int = _
  var maxChests: Int = 5
  var maxBeds: Int = 1
  var maxLanterns: Int = 1
  var contends: Int = _
  var time: Int = -1
  var oldTime: Int = _
  var lanternDamage = 0
  var updateTick: Int = _
  var needLightUpdate = true
  var lanternUpdateTick = 3
  var slide: Int = _
  var maxSlide = 144
  var chestTracker: Int = _
  var lanternTracker: Int = _
  var color = 15
  var occupied = false
  
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
    val id = Block.getBlockFromItem(stack.getItem())
    if (id == contendList(0)) return addLentern(stack)
    if (id == contendList(1)) return addChests
    if (id == contendList(2)) return addBed
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
    val bb = TileEntity.INFINITE_EXTENT_AABB
    val bound = TileEntityTent.bounds(getRotation)
    AxisAlignedBB.fromBounds(bound.xMin, bound.yMin, bound.zMin, bound.xMax, bound.yMax, bound.zMax)
    bb
  }
  override def getSizeInventory(): Int = 151
  def initalize() {
    if (!worldObj.isRemote) {
      structures = Objs.tentStructure
      for (i <- 0 until 4) {
        tracker(i) = new BoundsTracker(bd.x, bd.y, bd.z, bounds(i))
      }
      isNew = false
    }
  }
  def isOccupied = occupied
  def setOccupied(occupied:Boolean) = this.occupied = occupied
  def getRotation = if(bd.block.eq(Objs.tent)) bd.state.getValue(Tent.FACING).asInstanceOf[EnumFacing].getHorizontalIndex else 0
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
    super[WithTileInventory].readFromNBT(tag)
    contends = tag.getInteger("contends")
    beds = tag.getInteger("beds")
    lanterns = tag.getInteger("lanterns")
    chests = tag.getInteger("chests")
    lanternDamage = tag.getInteger("lanternDamage")
    time = tag.getInteger("time")
    color = tag.getInteger("color")
    occupied = tag.getBoolean("occupied")
  }
  def removeAll() {
    setContends(0, 0, true, 2)
    setContends(0, 1, true, 0)
    setContends(0, 2, true, 0)
  }
  def removeBed(): Boolean = {
    if (beds > 0) {
      setContends(beds - 1, BEDS, true, 1)
      worldObj.dropItemInWorld(getContendsFor(Objs.sleepingBag), bd.x, bd.y, bd.z, new Random())
      return true
    }
    false
  }
  def removeChest(): Boolean = {
    if (chests > 0) {
      setContends(chests - 1, CHEST, true, 1)
      worldObj.dropItemInWorld(getContendsFor(Blocks.chest), bd.x, bd.y, bd.z, new Random())
      return true
    }
    false
  }
  def removeLantern(): Boolean = {
    if (lanterns > 0) {
      setContends(lanterns - 1, LANTERN, true, 1)
      worldObj.dropItemInWorld(getContendsFor(Objs.lantern), bd.x, bd.y, bd.z, new Random())
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
    if (drop == 1) worldObj.dropItemInWorld(getContendsFor(contendList(contendId)), bd.x, bd.y, bd.z, new Random())
    if (drop == 2) worldObj.dropItemsInWorld(getContends, bd.x, bd.y, bd.z, new Random())
    if (sendData) sendTileData(1, !worldObj.isRemote, contendNum, contendId, drop)
    if (contendId == LANTERN) lanterns = contendNum
    if (contendId == CHEST) chests = contendNum
    if (contendId == BEDS) beds = contendNum
    contends = (beds * bedCost) + (chests * chestCost) + (lanterns * lanternCost)
    sendTileData(2, !worldObj.isRemote, contends)
    bd.update
    bd.updateRender
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
    if(!worldObj.isRemote) {
      if (getRotation == 0) bd.south.block.asInstanceOf[TentBounds].sleep(bd.south, player)
      else if (getRotation == 1) bd.west.block.asInstanceOf[TentBounds].sleep(bd.west, player)
      else if (getRotation == 2) bd.north.block.asInstanceOf[TentBounds].sleep(bd.north, player)
      else if (getRotation == 3) bd.east.block.asInstanceOf[TentBounds].sleep(bd.east, player)
    } else {
      PacketSender.toServer(new PlayerSleepInTent(bd.x, bd.y, bd.z))
    }
  }
  def createStructure {
    if (!worldObj.isRemote) {
      if (isNew) initalize()
      structures(getRotation).createStructure(worldObj, tracker(getRotation), Objs.tentBounds.getDefaultState.withProperty(TentBounds.FACING, bd.state.getValue(Tent.FACING)))
    }
  }
  override def update() {
    if (!worldObj.isRemote) {
      oldTime = time
      if (chestTracker != chests) {
        chestTracker = chests
        worldObj.dropItemsInWorld(getExcesChestContends, bd.x, bd.y, bd.z, new Random())
      }
      if (lanternTracker != lanterns) {
        lanternTracker = lanterns
        if (lanterns == 0) {
          if (getStackInSlot(0) != null) {
            worldObj.dropItemInWorld(getStackInSlot(0), bd.x, bd.y, bd.z, new Random())
          }
          setInventorySlotContents(0, null)
        }
      }
      if (needLightUpdate) {
        bd.update
        bd.updateRender
        if (lanternUpdateTick == 0) needLightUpdate = false
        else if (lanternUpdateTick > 0) lanternUpdateTick -= 1
      }
      if (isNew) initalize()
      updateTick += 1
      if ((updateTick > 10) && (time > 0)) {
        time -= 1
        updateTick = 0
      }
      if (time == 0) {
        time = -1
        lanternDamage = 1
        sendTileData(3, true, lanternDamage)
        bd.update
        bd.updateRender
      }
      if ((time <= 0) && (getStackInSlot(0) != null)) {
        decrStackSize(0, 1)
        time = 1500
        lanternDamage = 0
        sendTileData(3, true, lanternDamage)
        bd.update
        bd.updateRender
      }
      if (time != oldTime) {
        sendTileData(5, true, time)
      }
    }
  }
  override def writeToNBT(tag: NBTTagCompound) {
    super.writeToNBT(tag)
    tag.setInteger("contends", contends)
    tag.setBoolean("occupied", occupied)
    tag.setInteger("beds", beds)
    tag.setInteger("lanterns", lanterns)
    tag.setInteger("chests", chests)
    tag.setInteger("lanternDamage", lanternDamage)
    tag.setInteger("time", time)
    tag.setInteger("color", color)
    super[WithTileInventory].writeToNBT(tag)
  }
}