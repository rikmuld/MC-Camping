package com.rikmuld.camping.tileentity

import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib.AdvancementInfo
import com.rikmuld.camping.entity.Mountable
import com.rikmuld.camping.inventory.SlotCooking
import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.objs.Definitions.CampfireWood._
import com.rikmuld.camping.objs.Registry
import com.rikmuld.corerm.advancements.TriggerHelper
import com.rikmuld.corerm.objs.blocks.BlockSimple
import com.rikmuld.corerm.tileentity.{TileEntityInventory, TileEntitySimple, TileEntityTicker}
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.{Potion, PotionEffect}
import net.minecraft.util.math.{AxisAlignedBB, BlockPos}
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

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
    Registry.campfireWoodOn

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
    Registry.campfireWood

  override def shouldRefresh(world: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState): Boolean =
    oldState.getBlock != newState.getBlock
}

trait Roaster {
  def canRoast(item: ItemStack): Boolean =
    true

  protected def roastResult(item:ItemStack): ItemStack =
    new ItemStack(Registry.marshmallow)

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

class TileCampfireCook extends TileEntityInventory with Roaster with TileEntityTicker {
  private var fuel: Int =
    0

  private var cookProgress: Array[Int] =
    new Array[Int](10)

  private var equipment: Option[CookingEquipment] =
    None

  private var slots: Option[Seq[SlotCooking]] =
    None

  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox: AxisAlignedBB =
    new AxisAlignedBB(pos.getX, pos.getY, pos.getZ, pos.getX + 1, pos.getY + 1, pos.getZ + 1)

  override def shouldRefresh(world:World, pos:BlockPos, old:IBlockState, nw:IBlockState): Boolean =
    old.getBlock != nw.getBlock

  override def readFromNBT(tag: NBTTagCompound) {
    super.readFromNBT(tag)

    fuel = tag.getInteger("fuel")
    cookProgress = tag.getIntArray("cookProgress")
  }

  override def writeToNBT(tag: NBTTagCompound):NBTTagCompound = {
    tag.setInteger("fuel", fuel)
    tag.setIntArray("cookProgress", cookProgress)

    super.writeToNBT(tag)
  }

  override def canRoast(item:ItemStack): Boolean =
    fuel > 0

  override def roastSpeed(item:ItemStack): Float =
    0.75f + (getCoalPieces / 12f)

  override def getSizeInventory: Int =
    12

  override def getName: String =
    "campfire_cooking"

  override def closeInventory(player: EntityPlayer): Unit =
    slots = None

  def getCoalPieces: Int = //TODO test
    Math.ceil(fuel / config.campfireCoalFuel.toFloat).toInt

  def getEquipment: Option[CookingEquipment] =
    equipment

  def getScaledFuel(maxPixels: Int): Float =
    (fuel.toFloat / config.campfireMaxFuel) * maxPixels

  def getScaledCookProgress(maxPixels: Int, slot: Int): Float =
    equipment.fold(0f)(equip =>
      (cookProgress(slot).toFloat + 1) / equip.getCookTime
    ) * maxPixels

  def getCookProcess(slot: Int): Int =
    cookProgress(slot)

  def setCookProcess(slot: Int, data: Int): Unit =
    cookProgress(slot) = data

  def setSlots(newSlots: Seq[SlotCooking]): Unit =
    slots = Some(newSlots)

  override def setTileData(id: Int, data: Seq[Int]): Unit = id match {
    case 0 =>
      fuel = data.head
    case 1 =>
      cookProgress(data.head) = data(1)//TODO test how efficiency holds up agains send and detect changes progress bar update stuff in container since no tile data has to be send
      //TODO regardless actually change to that since better way
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
          PacketSender.sendToClient(new ItemsData(i + 2, bd.x, bd.y, bd.z, getStackInSlot(i + 2)))
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
  def manageCookingEquipment() {
    var flag = false
    if ((equipment == null) && (!getStackInSlot(1).isEmpty)) {
      equipment = CookingEquipment.getCooking(getStackInSlot(1))
    } else if (equipment != null){
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
          if (!slots.get(i).getStack.isEmpty) WorldUtils.dropItemInWorld(bd.world, slots.get(i).getStack, bd.pos)
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
    if (((fuel + config.campfireCoalFuel) <= maxFeul) && !getStackInSlot(0).isEmpty) {
      decrStackSize(0, 1)
      fuel += config.campfireCoalFuel
    }
  }

  override def update(){
    var equipOld = equipment
    manageCookingEquipment()
    if (!world.isRemote) {
      oldFuel = fuel
      manageFuel()
      cookFood()
      if(fuel != oldFuel){
        if(fuel == 0)Objs.campfireCook.asInstanceOf[CampfireCook].setOn(getWorld, pos, false)
        else if(oldFuel == 0)Objs.campfireCook.asInstanceOf[CampfireCook].setOn(getWorld, pos, true)
      }
    }
  }
}