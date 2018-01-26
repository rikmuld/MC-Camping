package com.rikmuld.camping.objs.tile

import java.util.{ArrayList, Random}

import com.rikmuld.camping.inventory.SlotState
import com.rikmuld.camping.objs.{BlockDefinitions, Objs}
import com.rikmuld.camping.objs.block.{Tent, TentBounds}
import com.rikmuld.camping.objs.misc.PlayerSleepInTent
import com.rikmuld.camping.objs.tile.TileEntityTent._
import com.rikmuld.corerm.features.bounds.{Bounds, BoundsStructure, BoundsTracker}
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.corerm.tileentity.{TileEntityInventory, TileEntitySimple}
import com.rikmuld.corerm.utils.CoreUtils._
import com.rikmuld.corerm.utils.WorldBlock._
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.{EnumFacing, ITickable}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object TileEntityTent {
  var bounds: Array[Bounds] = Array(new Bounds(-0.5F, 0, 0, 1.5F, 1.5F, 3), new Bounds(-2, 0, -0.5F, 1, 1.5F, 1.5F), new Bounds(-0.5F, 0, -2, 1.5F, 1.5F, 1), new Bounds(0, 0, -0.5F, 3, 1.5F, 1.5F))
  var LANTERN: Int = 0
  var CHEST: Int = 1
  var BEDS: Int = 2
}

class TileTent extends TileEntitySimple with TileEntityInventory with ITickable {
  final val COST_CHEST = 2
  final val COST_BED = 5
  final val COST_LANTERN = 1
  final val MAX_COST = 10
  final val MAX_CHESTS: Int = 5
  final val MAX_BEDS: Int = 1
  final val MAX_LANTERNS: Int = 1
  final val ALOWED_ITEMS = Array(Objs.lantern, Blocks.CHEST, Objs.sleepingBag)

  var slots: Array[Array[SlotState]] = _
  var structures: Array[BoundsStructure] = _
  var tracker = new Array[BoundsTracker](4)
  var isNew = true
  var dropped = false
  var needLightUpdate = true
  var occupied = false
  var chests: Int = _
  var beds: Int = _
  var lanterns: Int = _
  var contends: Int = _
  var oldTime: Int = _
  var updateTick: Int = _
  var slide: Int = _
  var chestTracker: Int = _
  var lanternTracker: Int = _
  var lanternDamage = 0
  var time: Int = -1
  var lanternUpdateTick = 3
  var maxSlide = 144
  var color = 15
  
  def addBed(): Boolean = {
    if (((contends + COST_BED) <= MAX_COST) && (beds < MAX_BEDS)) {
      setContends(beds + 1, BEDS, true, 0)
      return true
    }
    false
  }
  def addChests(): Boolean = {
    if (((contends + COST_CHEST) <= MAX_COST) && (chests < MAX_CHESTS)) {
      setContends(chests + 1, CHEST, true, 0)
      return true
    }
    false
  }
  def addContends(stack: ItemStack): Boolean = {
    val id = Block.getBlockFromItem(stack.getItem())
    if (id == ALOWED_ITEMS(0)) return addLentern(stack)
    if (id == ALOWED_ITEMS(1)) return addChests
    if (id == ALOWED_ITEMS(2)) return addBed
    false
  }
  def addLentern(stack: ItemStack): Boolean = {
    if (((contends + COST_LANTERN) <= MAX_COST) && (lanterns < MAX_LANTERNS)) {
      time = if (stack.hasTagCompound()) stack.getTagCompound.getInteger("time") else -1
      lanternDamage = if (time > 0) BlockDefinitions.Lantern.ON else BlockDefinitions.Lantern.OFF

      sendTileData(3, true, lanternDamage)
      setContends(lanterns + 1, LANTERN, true, 0)
      return true
    }
    false
  }
  def getContends(): ArrayList[ItemStack] = {
    val stacks = new ArrayList[ItemStack]()
    val lanternStack = new ItemStack(ALOWED_ITEMS(0), lanterns, if (time > 0) BlockDefinitions.Lantern.ON else BlockDefinitions.Lantern.OFF)
    if (time > 0) {
      lanternStack.setTagCompound(new NBTTagCompound())
      lanternStack.getTagCompound.setInteger("time", time)
    }
    if (lanterns > 0) stacks.add(lanternStack)
    if (chests > 0) stacks.add(new ItemStack(ALOWED_ITEMS(1), chests, 0))
    if (beds > 0) stacks.add(new ItemStack(ALOWED_ITEMS(2), beds, 0))
    stacks
  }
  def getContendsFor(block: Block): ItemStack = {
    if (block == Objs.lantern) {
      val lanternStack = new ItemStack(ALOWED_ITEMS(0), lanterns, if (time > 0) BlockDefinitions.Lantern.ON else BlockDefinitions.Lantern.OFF)
      if (time > 0) {
        lanternStack.setTagCompound(new NBTTagCompound())
        lanternStack.getTagCompound.setInteger("time", time)
      }
      return lanternStack
    }
    if (block == Blocks.CHEST) return new ItemStack(ALOWED_ITEMS(1), 1, 0)
    if (block == Objs.sleepingBag) return new ItemStack(ALOWED_ITEMS(2), 1, 0)
    null
  }
  
  private def getExcesChestContends(): ArrayList[ItemStack] = {
    val list = new ArrayList[ItemStack]()
    for (i <- chests * 5 * 6 until 150 if !getStackInSlot(i + 1).isEmpty) {
      list.add(getStackInSlot(i + 1))
      setInventorySlotContents(i + 1, ItemStack.EMPTY)
    }
    list
  }
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox(): AxisAlignedBB = TileEntity.INFINITE_EXTENT_AABB
  override def getSizeInventory(): Int = 151
  def initalize() {
    if (!world.isRemote) {
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
    super[TileEntityInventory].readFromNBT(tag)
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
      world.dropItemInWorld(getContendsFor(Objs.sleepingBag), bd.x, bd.y, bd.z, new Random())
      return true
    }
    false
  }
  def removeChest(): Boolean = {
    if (chests > 0) {
      setContends(chests - 1, CHEST, true, 1)
      world.dropItemInWorld(getContendsFor(Blocks.CHEST), bd.x, bd.y, bd.z, new Random())
      return true
    }
    false
  }
  def removeLantern(): Boolean = {
    if (lanterns > 0) {
      setContends(lanterns - 1, LANTERN, true, 1)
      world.dropItemInWorld(getContendsFor(Objs.lantern), bd.x, bd.y, bd.z, new Random())
      return true
    }
    false
  }
  def setColor(color: Int) {
    if (!world.isRemote) {
      this.color = color
      sendTileData(6, true, color)
    }
  }
  def setContends(contendNum: Int, contendId: Int, sendData: Boolean, drop: Int) {
    if (drop == 1) world.dropItemInWorld(getContendsFor(ALOWED_ITEMS(contendId)), bd.x, bd.y, bd.z, new Random())
    if (drop == 2) world.dropItemsInWorld(getContends, bd.x, bd.y, bd.z, new Random())
    if (sendData) sendTileData(1, !world.isRemote, contendNum, contendId, drop)
    if (contendId == LANTERN) lanterns = contendNum
    if (contendId == CHEST) chests = contendNum
    if (contendId == BEDS) beds = contendNum
    contends = (beds * COST_BED) + (chests * COST_CHEST) + (lanterns * COST_LANTERN)
    sendTileData(2, !world.isRemote, contends)

    if(lanterns > 0 && time > 0) Objs.tent.asInstanceOf[Tent].setOn(getWorld, pos, true)
    else Objs.tent.asInstanceOf[Tent].setOn(getWorld, pos, false)

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
    else if (id == 2) {
      contends = data(0)
    }
    else if (id == 3) {
      lanternDamage = data(0)
    }
    else if (id == 4) {
      slide = data(0)
      manageSlots()
    } else if (id == 5) time = data(0)
    else if (id == 6) color = data(0)
  }
  def sleep(player: EntityPlayer) {
    if(!world.isRemote) {
      if (getRotation == 0) bd.south.block.asInstanceOf[TentBounds].sleep(bd.south, player)
      else if (getRotation == 1) bd.west.block.asInstanceOf[TentBounds].sleep(bd.west, player)
      else if (getRotation == 2) bd.north.block.asInstanceOf[TentBounds].sleep(bd.north, player)
      else if (getRotation == 3) bd.east.block.asInstanceOf[TentBounds].sleep(bd.east, player)
    } else {
      PacketSender.toServer(new PlayerSleepInTent(bd.x, bd.y, bd.z))
    }
  }
  def createStructure {
    if (!world.isRemote) {
      if (isNew) initalize()
      structures(getRotation).createStructure(world, tracker(getRotation), Objs.tentBounds.getDefaultState.withProperty(TentBounds.FACING, bd.state.getValue(Tent.FACING)))
    }
  }
  override def update() {
    if (!world.isRemote) {
      oldTime = time
      if (chestTracker != chests) {
        chestTracker = chests
        world.dropItemsInWorld(getExcesChestContends, bd.x, bd.y, bd.z, new Random())
      }
      if (lanternTracker != lanterns) {
        lanternTracker = lanterns
        if (lanterns == 0) {
          if (!getStackInSlot(0).isEmpty) {
            world.dropItemInWorld(getStackInSlot(0), bd.x, bd.y, bd.z, new Random())
          }
          setInventorySlotContents(0, ItemStack.EMPTY)
        }
      }
      if (isNew) initalize()
      updateTick += 1
      if ((updateTick > 10) && (time > 0)) {
        time -= 1
        updateTick = 0
      }
      if (time == 0) {
        time = -1
        lanternDamage = BlockDefinitions.Lantern.OFF
        Objs.tent.asInstanceOf[Tent].setOn(getWorld, pos, true)
        sendTileData(3, true, lanternDamage)
        bd.update
        bd.updateRender
      }
      if ((time <= 0) && (!getStackInSlot(0).isEmpty)) {
        decrStackSize(0, 1)
        time = 1500
        lanternDamage = BlockDefinitions.Lantern.ON
        sendTileData(3, true, lanternDamage)
        Objs.tent.asInstanceOf[Tent].setOn(getWorld, pos, true)
        bd.update
        bd.updateRender
      }
      if (time != oldTime) sendTileData(5, true, time)
    }
  }

  override def shouldRefresh(world:World, pos:BlockPos, oldState:IBlockState, newState:IBlockState) =
    oldState.getBlock != newState.getBlock
  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound = {
    tag.setInteger("contends", contends)
    tag.setBoolean("occupied", occupied)
    tag.setInteger("beds", beds)
    tag.setInteger("lanterns", lanterns)
    tag.setInteger("chests", chests)
    tag.setInteger("lanternDamage", lanternDamage)
    tag.setInteger("time", time)
    tag.setInteger("color", color)
    super[TileEntityInventory].writeToNBT(tag)
    super.writeToNBT(tag)   
  }
}