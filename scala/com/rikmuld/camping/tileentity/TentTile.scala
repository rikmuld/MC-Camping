package com.rikmuld.camping.tileentity

import com.rikmuld.camping.inventory.SlotState
import com.rikmuld.camping.objs.Definitions
import com.rikmuld.camping.objs.blocks.Tent
import com.rikmuld.camping.registers.ObjRegistry
import com.rikmuld.camping.tileentity.SeqUtils._
import com.rikmuld.camping.tileentity.TileEntityTent._
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

//TODO move
object SeqUtils {
  def merge[A, B, C](b: Seq[B])(f: (A, B) => C)(a: Seq[A]): Seq[C] =
    merge(a, b)(f)

  def merge[A, B, C](a: Seq[A], b: Seq[B])(f: (A, B) => C): Seq[C] =
    a zip b map(t => f(t._1, t._2))

  def allOne(all: Int => Boolean)(one: Int => Boolean)(v: Seq[Int]): Boolean =
    (v forall all) && (v exists one)
}

//TODO still old code from years ago, rewrite
//TODO fix not scrollable
//TODO content does not drop when breaking
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

  private var slots: Option[Seq[Seq[SlotState]]] =
    None

  private var slide: Int =
    0

  def contentIdx(stack: ItemStack): Option[Int] =
    Option(ITEMS.indexOf(stack.getItem)).find(_ >= 0)

  def canAdd(stack: ItemStack): Boolean =
    contentIdx(stack) exists canAdd

  def canAdd(i: Int): Boolean = {
    val configs = CONFIGS
      .map(merge(contents)(_ - _))
      .filter(allOne(_ >= 0)(_ > 0))

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
    tentChanged(i, old, result, action)
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

  def tentChanged(id: Int, old: Int, nw: Int, action: Option[ItemStack]): Unit = id match {
    case LANTERN =>
      val light = action.fold(0)(TileLantern.timeFromStack)

      if(action.isEmpty)
        dropItem(id)

      sendTileData(3, !world.isRemote, light)
      setLight(light)
    case _ if action.isEmpty =>
      dropItem(id)
    case _ =>
  }

  private def setLight(i: Int): Unit = {
    if(!world.isRemote)
      if (light > 0 && i == 0)
        getBlock.setState(world, pos, Definitions.Tent.STATE_ON, false)
      else if (light == 0 && i > 0)
        getBlock.setState(world, pos, Definitions.Tent.STATE_ON, true)

    light = Math.max(0, i)
  }

  def dropItem(i: Int): Unit =
    if(world.isRemote)
      sendTileData(4, false, i)
    else
      WorldUtils.dropItemInWorld(world, toStack(i, Some(1)), pos)

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

//  def isOccupied = occupied
//  def setOccupied(occupied:Boolean) = this.occupied = occupied
//  def manageSlots() {
//    if (slots != null) {
//      if (chests > 2) {
//        val scaledSlide = MathUtils.getScaledNumber(slide, 144, (5 * chests) - 11).toInt
//        for (i <- 0 until (5 * chests); j <- 0 until 6) {
//          slots(i)(j).setStateX(scaledSlide)
//          if ((i < scaledSlide) || (i >= (scaledSlide + 11))) {
//            slots(i)(j).disable
//          } else {
//            slots(i)(j).enable
//          }
//        }
//      } else {
//        for (i <- 0 until (5 * chests); j <- 0 until 6) {
//          slots(i)(j).setStateX(0)
//          slots(i)(j).enable()
//        }
//      }
//    }
//  }
//
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

//  def setSlideState(slideState: Int) {
//    slide = slideState
//    manageSlots()
//    sendTileData(4, false, slideState)
//  }
//  def setSlots(slots: Array[Array[SlotState]]) = this.slots = slots

  override def setTileData(id: Int, data: Seq[Int]): Unit = {
    super.setTileData(id, data)

    id match {
      case i if i < 3 =>
        contents(i) = data.head
      case 3 =>
        setLight(data.head)
      case 4 =>
        dropItem(data.head)
//    case 5 =>
//      slide = data(0)
//      manageSlots()
      case 6 =>
        setColor(data.head)
    }
  }

  //
  //  def sleep(player: EntityPlayer) {
  //    if(!world.isRemote) {
  ////      if (getRotation == 0) bd.south.block.asInstanceOf[TentBounds].sleep(bd.south, player)
  ////      else if (getRotation == 1) bd.west.block.asInstanceOf[TentBounds].sleep(bd.west, player)
  ////      else if (getRotation == 2) bd.north.block.asInstanceOf[TentBounds].sleep(bd.north, player)
  ////      else if (getRotation == 3) bd.east.block.asInstanceOf[TentBounds].sleep(bd.east, player)
  //    } else {
  ////      PacketSender.sendToServer(new PlayerSleepInTent(pos.getX, pos.getY, pos.getZ))
  //    }
  //  }
  //
  //
  override def onChange(slot: Int): Unit =
    super.onChange(slot)
  //if equals glowstone slot, add lantern light

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