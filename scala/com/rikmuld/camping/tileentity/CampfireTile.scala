package com.rikmuld.camping.tileentity

import com.rikmuld.camping.Lib.AdvancementInfo
import com.rikmuld.camping.entity.Mountable
import com.rikmuld.camping.objs.Definitions.CampfireWood._
import com.rikmuld.camping.objs.Registry
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.objs.blocks.BlockSimple
import com.rikmuld.corerm.tileentity.{TileEntitySimple, TileEntityTicker}
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.{Potion, PotionEffect}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World

import scala.collection.JavaConversions._

object TileCampfireWood {
  lazy val effects: Seq[PotionEffect] =
    for (i <- Seq(20, 5, 19, 11, 16, 9, 1, 18, 15, 10, 17, 3, 13, 10, 12))
      yield new PotionEffect(Potion.getPotionById(i), 400, 0)
}

class TileCampfireWoodOn extends TileEntitySimple with TileEntityTicker with Roaster {
  registerTicker(updateEffects, 100)

  def getEntityBounds: AxisAlignedBB =
    getBlock.getBoundingBox(world.getBlockState(pos), world, pos).offset(pos).grow(8)

  def updateEffects(): Unit = {
    val light = getBlock.getInt(world, pos, STATE_LIGHT)

    if (!world.isRemote && light < 15) {
      world.getEntitiesWithinAABB(classOf[EntityLivingBase], getEntityBounds).foreach(entity =>
        entity.addPotionEffect(new PotionEffect(TileCampfireWood.effects(light)))
      )
    }
  }

  def getBlock: BlockSimple =
    Registry.campfireWood

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean =
    oldState.getBlock != newState.getBlock

  override def canRoast(item: ItemStack): Boolean =
    true

  override def roastResult(item: ItemStack) =
    new ItemStack(Registry.marshmallow)

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
    Registry.campfireWood

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean =
    oldState.getBlock != newState.getBlock
}

trait Roaster {
  def canRoast(item: ItemStack): Boolean

  protected def roastResult(item:ItemStack): ItemStack

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

class TileCampfireCook extends TileEntitySimple {}//with TileEntityInventory with ITickable with Roaster {
//  var maxFeul: Int = config.campfireMaxFuel
//  var fuelForCoal: Int = config.campfireCoalFuel
//  var fuel: Int = _
//  var oldFuel: Int = _
//  var coals: Array[Array[Float]] = Array.ofDim[Float](3, 20)
//  var rand: Random = new Random()
//  var cookProgress: Array[Int] = new Array[Int](10)
//  var oldCookProgress: Array[Int] = new Array[Int](10)
//  var equipment: CookingEquipment = _
//  var slots: Seq[SlotCooking] = _
//  var lastPlayer:EntityPlayer = _
//
//  for (i <- 0 until 20) {
//    coals(0)(i) = rand.nextFloat() / 5F
//    coals(1)(i) = rand.nextFloat() / 5F
//    coals(2)(i) = rand.nextFloat() * 360
//  }
//
//  override def getName: String =
//    "campfire_cooking"
//
//  override def canRoast(item:ItemStack):Boolean = {
//    this.fuel > 0 &&
//      (item.getItem, item.getItemDamage) == (Objs.parts, ItemDefinitions.Parts.MARSHMALLOW_STICK)
//  }
//  override def roastResult(item:ItemStack) = new ItemStack(Objs.marshmallow)
//  override def roastSpeed(item:ItemStack) = ((Math.log10((100*fuel)/maxFeul + 1) + 0.25)/2).toFloat
//  override def roastTime(item:ItemStack):Int = 150
//
//  def getEquipment: Option[CookingEquipment] =
//    Option(equipment)
//
//  private def cookFood() {
//    if (equipment != null) {
//      for (i <- 0 until equipment.maxFood) {
//        oldCookProgress(i) = cookProgress(i)
//        if (cookProgress(i) >= equipment.cookTime) {
//          cookProgress(i) = 0
//          if (equipment.canCook(getStackInSlot(i + 2))) {
//            if (equipment.getCookedFood(getStackInSlot(i + 2)) != null) setInventorySlotContents(i + 2, equipment.getCookedFood(getStackInSlot(i + 2)).copy())
//            else setInventorySlotContents(i + 2, new ItemStack(Objs.parts, 1, ItemDefinitions.Parts.ASH))
//          } else setInventorySlotContents(i + 2, new ItemStack(Objs.parts, 1, ItemDefinitions.Parts.ASH))
//          PacketSender.sendToClient(new ItemsData(i + 2, bd.x, bd.y, bd.z, getStackInSlot(i + 2)))
//        }
//        if (fuel > 0) {
//          if (!getStackInSlot(i + 2).isEmpty &&
//            (!(getStackInSlot(i + 2).getItem == Objs.parts) ||
//              getStackInSlot(i + 2).getItemDamage != ItemDefinitions.Parts.ASH)) {
//            cookProgress(i) += 1
//          }
//        } else if (cookProgress(i) > 0) cookProgress(i) = 0
//        if (getStackInSlot(i + 2).isEmpty && (cookProgress(i) > 0)) cookProgress(i) = 0
//        if (oldCookProgress(i) != cookProgress(i)) sendTileData(1, true, cookProgress(i), i)
//      }
//    }
//  }
//  def getCoalPieces(): Int = if (fuel > 0) (if (((fuel / fuelForCoal) + 1) <= 20) (fuel / fuelForCoal) + 1 else 20) else 0
//  @SideOnly(Side.CLIENT)
//  override def getRenderBoundingBox(): AxisAlignedBB = new AxisAlignedBB(bd.x, bd.y, bd.z, bd.x + 1, bd.y + 1, bd.z + 1)
//  def getScaledCoal(maxPixels: Int): Float = (fuel.toFloat / maxFeul.toFloat) * maxPixels
//  def getScaledcookProgress(maxPixels: Int, foodNum: Int): Float = ((cookProgress(foodNum).toFloat + 1) / equipment.cookTime) * maxPixels
//  override def getSizeInventory(): Int = 12
//
//  def manageCookingEquipment() {
//    var flag = false
//    if ((equipment == null) && (!getStackInSlot(1).isEmpty)) {
//      equipment = CookingEquipment.getCooking(getStackInSlot(1))
//    } else if (equipment != null){
//      if(getStackInSlot(1).isEmpty){
//        equipment = null
//      } else {
//        val newEquip = CookingEquipment.getCooking(getStackInSlot(1))
//        if(newEquip != equipment){
//          flag = true
//          equipment = newEquip
//        }
//      }
//    }
//
//    if (slots != null) {
//      if (equipment == null || flag) {
//        for (i <- 0 until 10 if slots.get(i).active) {
//          slots.get(i).deActivate()
//          if (!slots.get(i).getStack.isEmpty) WorldUtils.dropItemInWorld(bd.world, slots.get(i).getStack, bd.pos)
//        }
//      }
//      if (equipment != null) {
//        for (i <- 0 until equipment.maxFood if !slots.get(i).active) slots.get(i).activate(equipment.slots(0)(i), equipment.slots(1)(i), equipment, this)
//      }
//    }
//  }
//  def bd: BlockData =
//    (world, pos)
//  def manageFuel() {
//    if (fuel > 0) {
//      fuel -= 1
//      sendTileData(0, true, fuel)
//    }
//    if (((fuel + fuelForCoal) <= maxFeul) && !getStackInSlot(0).isEmpty) {
//      decrStackSize(0, 1)
//      fuel += fuelForCoal
//    }
//  }
//  override def readFromNBT(tag: NBTTagCompound) {
//    super[TileEntitySimple].readFromNBT(tag)
//    super[TileEntityInventory].readFromNBT(tag)
//
//    fuel = tag.getInteger("fuel")
//    cookProgress = tag.getIntArray("cookProgress")
//
//    for (i <- 0 until coals.length; j <- 0 until coals(i).length) coals(i)(j) = tag.getFloat("coals" + i + j)
//  }
//  def setSlots(newSlots: Seq[SlotCooking]) =
//    slots = newSlots
//
//  override def setTileData(id: Int, data: Seq[Int]) {
//    if (id == 0) fuel = data(0)
//    if (id == 1) cookProgress(data(1)) = data(0)
//  }
//  override def shouldRefresh(world:World, pos:BlockPos, oldState:IBlockState, newState:IBlockState) = oldState.getBlock != newState.getBlock
//  override def update(){
//    var equipOld = equipment
//    manageCookingEquipment()
//    if (!world.isRemote) {
//      oldFuel = fuel
//      manageFuel()
//      cookFood()
//      if(fuel != oldFuel){
//        if(fuel == 0)Objs.campfireCook.asInstanceOf[CampfireCook].setOn(getWorld, pos, false)
//        else if(oldFuel == 0)Objs.campfireCook.asInstanceOf[CampfireCook].setOn(getWorld, pos, true)
//      }
//    }
//  }
//  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound = {
//    tag.setInteger("fuel", fuel)
//    tag.setIntArray("cookProgress", cookProgress)
//    for (i <- 0 until coals.length; j <- 0 until coals(i).length) tag.setFloat("coals" + i + j, coals(i)(j))
//    super[TileEntityInventory].writeToNBT(tag)
//    super[TileEntitySimple].writeToNBT(tag)
//  }
//}