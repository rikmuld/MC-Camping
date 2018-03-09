package com.rikmuld.camping.tileentity

import com.rikmuld.camping.misc.PlayerSleepInTent
import com.rikmuld.camping.objs.Definitions
import com.rikmuld.camping.objs.blocks.Tent
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.tileentity.TileEntityTent._
import com.rikmuld.camping.{SeqUtils, Utils}
import com.rikmuld.corerm.network.PacketSender
import com.rikmuld.corerm.objs.blocks.BlockSimple
import com.rikmuld.corerm.tileentity.{TileEntityInventory, TileEntityTicker}
import com.rikmuld.corerm.utils.WorldUtils
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.JavaConversions._

object TileEntityTent {
  final val CONFIGS = Vector(
    Vector(0, 5, 0),
    Vector(1, 4, 0),
    Vector(1, 2, 1)
  )

  final val ITEMS = Vector(
    ObjRegistry.lantern,
    Blocks.CHEST,
    ObjRegistry.sleepingBag
  ) map Item.getItemFromBlock

  final val LANTERN = 0
  final val CHESTS = 1
  final val BED = 2
}

class TileTent extends TileEntityInventory with TileEntityTicker {
  registerTicker(tickLight, 20)

  val contents: Array[Int] =
    Array.ofDim(3)

  private var color: Int =
    15

  private var light: Int =
    0

  private var occupied: Boolean =
    false

  def contentIdx(stack: ItemStack): Option[Int] =
    Option(ITEMS.indexOf(stack.getItem)).find(_ >= 0)

  def canAdd(stack: ItemStack): Boolean =
    contentIdx(stack) exists canAdd

  def canAdd(i: Int): Boolean = {
    val configs = CONFIGS
      .map(SeqUtils.merge(contents)(_ - _))
      .filter(SeqUtils.allOne(_ >= 0)(_ > 0))

    if(configs.nonEmpty)
      configs.transpose.get(i).max > 0
    else
      false
  }

  def add(stack: ItemStack): Boolean =
    contentIdx(stack) exists(add(_, Some(stack)))

  def add(i: Int, action: Option[ItemStack]): Boolean =
    if(canAdd(i)) {
      update(i, _ + 1, action)
      true
    } else
      false

  def remove(stack: ItemStack): Unit =
    contentIdx(stack) foreach remove

  def remove(i: Int): Unit =
    if(contents(i) > 0)
      update(i, _ - 1, None)

  def update(i: Int, f: Int => Int, action: Option[ItemStack]): Unit = {
    val old = contents(i)
    val result = f(old)

    contents(i) = result

    sendTileData(i, !world.isRemote, result)
    tentChanged(i, action)
  }

  def count(i: Int): Int =
    contents(i)

  def toStacks: Seq[ItemStack] =
    contents.indices map(toStack(_))

  def toStack(i: Int, n: Option[Int] = None): ItemStack = (i, n.getOrElse(count(i))) match {
    case (_, 0) =>
      ItemStack.EMPTY
    case (LANTERN, count) =>
      TileLantern.stackFromTime(light, count)
    case (_, count) =>
      new ItemStack(ITEMS(i), count)
  }

  def tentChanged(id: Int, action: Option[ItemStack]): Unit = action match {
    case None =>
      sendTileData(4, false, id)
    case Some(stack) =>
      contentAdded(id, stack)
  }

  def contentAdded(id: Int, stack: ItemStack): Unit = id match {
    case LANTERN =>
      val light = TileLantern.timeFromStack(stack)

      sendTileData(3, true, light)
      setLight(light)
    case _ =>
  }

  def contentRemoved(id: Int): Unit = id match {
      case CHESTS =>
        val chests = count(id)
        val stacks = getInventory.slice(1 + 27 * chests, 1 + 27 * (chests + 1))

        WorldUtils.dropItemsInWorld(world, stacks, pos)
        WorldUtils.dropItemInWorld(world, toStack(id, Some(1)), pos)
      case LANTERN =>
        val stack =  TileLantern.stackFromTime(light, 1)

        WorldUtils.dropItemInWorld(world, stack, pos)
        sendTileData(3, true, 0)
        setLight(0)
      case BED =>
        WorldUtils.dropItemInWorld(world, toStack(id, Some(1)), pos)
  }


  private def setLight(i: Int): Unit = {
    if(!world.isRemote)
      if (i == 0)
        getBlock.setState(world, pos, Definitions.Tent.STATE_ON, false)
      else if (light == 0 && i > 0)
        getBlock.setState(world, pos, Definitions.Tent.STATE_ON, true)

    light = Math.max(0, i)
  }

  def getLight: Int =
    light

  def tickLight(): Unit = {
    if (light > 0) {
      setLight(light - 1)
      sendTileData(3, true, light)
    }
  }

  def getBlock: BlockSimple =
    ObjRegistry.tent

  override def getName: String =
    "tent"

  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox: AxisAlignedBB =
    ObjRegistry.tent.asInstanceOf[Tent].getStructure(Some(world.getBlockState(pos))).getBounds.bounds.offset(pos)

  override def getSizeInventory: Int =
    151

  def isOccupied: Boolean =
    occupied

  def setOccupied(occupied:Boolean): Unit =
    this.occupied = occupied

  def getFacing: EnumFacing =
    getBlock.getFacing(world.getBlockState(pos))

  override def readFromNBT(tag: NBTTagCompound): Unit = {
    super.readFromNBT(tag)

    setContents(tag.getIntArray("contents"))

    color = tag.getInteger("color")
    occupied = tag.getBoolean("occupied")
  }

  def setContents(nw: Array[Int]): Unit = {
    for(i <- nw.indices)
      contents(i) = nw(i)
  }

  def setColor(color: Int): Unit = {
    if (!world.isRemote)
      sendTileData(6, true, color)

    this.color = color
  }

  def getColor: Int =
    color

  override def setTileData(id: Int, data: Seq[Int]): Unit = {
    super.setTileData(id, data)

    id match {
      case i if i < 3 =>
        contents(i) = data.head
      case 3 =>
        setLight(data.head)
      case 4 =>
        contentRemoved(data.head)
      case 6 =>
        setColor(data.head)
    }
  }


  def sleep(player: EntityPlayer): Unit =
    if(!world.isRemote)
      Utils.trySleep(isOccupied, setOccupied)(world, pos, player)
    else
      PacketSender.sendToServer(new PlayerSleepInTent(pos.getX, pos.getY, pos.getZ))


  override def onChange(slot: Int): Unit = slot match {
    case 0 =>
      if(light < 10 && getStackInSlot(slot).getCount > 0) {
        setLight(750)
        decrStackSize(slot, 1)
      }
    case _ =>
  }

  override def shouldRefresh(world:World, pos:BlockPos, oldState:IBlockState, newState:IBlockState): Boolean =
    oldState.getBlock != newState.getBlock

  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound = {
    tag.setBoolean("occupied", occupied)
    tag.setIntArray("contents", contents)
    tag.setInteger("color", color)

    super.writeToNBT(tag)
  }

  override def init(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos): Unit =
    if(!world.isRemote)
      setColor(stack.getItemDamage)
}